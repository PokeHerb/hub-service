package org.pokeherb.hubservice.infrastructure.messaging;

import com.rabbitmq.client.Channel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
@EnableConfigurationProperties(RabbitHubProperties.class)
public class RabbitConsumer {

    private static final int MAX_RETRIES = 3;
    private final RabbitTemplate rabbitTemplate;
    private final MessageEventHandler handler;

    @RabbitListener(queues = "hub", containerFactory = "rabbitListenerContainerFactory")
    public void handleMessage(Message message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws Exception {
        String routingKey = getOriginalRoutingKey(message);
        String payload = new String(message.getBody(), StandardCharsets.UTF_8);

        try {
            log.info("Received routing key: {}", routingKey);

            switch (routingKey) {
                case "hub.created.order":
                    handler.handleOrderCreatedEvent(payload);
                    channel.basicAck(tag, false);
                    break;
                case "hub.canceled.order":
                    handler.handleOrderCanceledEvent(payload);
                    channel.basicAck(tag, false);
                    break;
                case "hub.retry.test":
                    throw new RuntimeException("Hub retry test");
                default:
                    log.info("알 수 없는 라우팅 키 : {}", routingKey);
                    channel.basicReject(tag, false);
                    break;
            }
        } catch (Exception e) {
            int retryCount = getRetryCount(message);
            if (retryCount >= MAX_RETRIES) {
                rabbitTemplate.convertAndSend(RabbitConfig.DLX, RabbitConfig.DLQ_ROUTING_KEY, message);
                channel.basicAck(tag, false);
                log.error("재시도 횟수 제한 초과, 메시지 처리 실패", e);
            } else {
                channel.basicNack(tag, false, false);
                log.warn("처리 실패, {}번째 재시도", retryCount + 1, e);
            }
        }
    }

    /**
     * 메시지의 x-death 헤더에서 현재까지의 재시도 횟수를 추출합니다.
     */
    private int getRetryCount(Message message) {
        MessageProperties properties = message.getMessageProperties();
        Map<String, Object> headers = properties.getHeaders();

        Object xDeathHeader = headers.get("x-death");

        if (xDeathHeader instanceof List<?> deathList) {

            if (!deathList.isEmpty()) {

                Object lastDeathEntry = deathList.getLast();

                if (lastDeathEntry instanceof Map<?, ?> deathMap) {
                    Object countObject = deathMap.get("count");

                    // count 필드는 Long 타입으로 들어오는 경우가 많으므로 안전하게 Number로 캐스팅합니다.
                    if (countObject instanceof Number) {
                        return ((Number) countObject).intValue();
                    }
                }
            }
        }

        // x-death 헤더가 없거나 파싱에 실패하면 초기 상태(재시도 횟수 0)로 간주합니다.
        return 0;
    }

    /**
     * 메시지의 Original Routing Key를 반환
     * - x-death 헤더가 존재하면, 리스트의 마지막 항목(가장 오래된 이벤트)에서 추출
     */
    private String getOriginalRoutingKey(Message message) {
        MessageProperties properties = message.getMessageProperties();
        Map<String, Object> headers = properties.getHeaders();
        Object xDeathHeader = headers.get("x-death");

        // x-death 헤더가 존재하고 List 형태인 경우
        if (xDeathHeader instanceof List<?> deathList) {

            if (!deathList.isEmpty()) {
                // 리스트의 마지막 항목(가장 오래된/최초 이벤트)을 참조
                Object firstDeathEntry = deathList.getLast();

                if (firstDeathEntry instanceof Map<?, ?> deathMap) {
                    // RabbitMQ가 기록한 라우팅 키 리스트
                    Object routingKeys = deathMap.get("routing-keys");

                    if (routingKeys instanceof List<?> routingKeyList) {
                        if (!routingKeyList.isEmpty()) {
                            // 리스트의 첫 번째 키를 반환 (예: "hub.retry.test")
                            return routingKeyList.getFirst().toString();
                        }
                    }
                }
            }
        }

        // x-death 헤더가 없거나 파싱에 실패하면, 현재 메시지가 받은 키를 반환
        return properties.getReceivedRoutingKey();
    }
}
