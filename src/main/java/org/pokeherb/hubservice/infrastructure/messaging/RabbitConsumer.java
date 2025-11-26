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
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
@EnableConfigurationProperties(RabbitHubProperties.class)
public class RabbitConsumer {

    private static final int MAX_RETRIES = 3;
    private final RabbitHubProperties rabbitHubProperties;
    private final RabbitTemplate rabbitTemplate;
    private final MessageEventHandler handler;

    @RabbitListener(queues = "hub", containerFactory = "rabbitListenerContainerFactory")
    public void handleMessage(Message message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws Exception {
        String routingKey = message.getMessageProperties().getReceivedRoutingKey();
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
                default:
                    log.info("알 수 없는 라우팅 키 : {}", routingKey);
                    channel.basicReject(tag, false);
                    break;
            }
        } catch (Exception e) {
            int retryCount = getRetryCount(message);
            if (retryCount >= MAX_RETRIES) {
                channel.basicNack(tag, false, false);
                log.error("재시도 횟수 제한 초과, 메시지 처리 실패 (원인 : {})", e.getMessage());
            } else {
                Map<String, Object> headers = message.getMessageProperties().getHeaders();
                headers.put("x-retry-count", retryCount + 1);
                rabbitTemplate.convertAndSend(rabbitHubProperties.exchange(), routingKey, msg -> {
                    msg.getMessageProperties().getHeaders().putAll(headers);
                    return msg;
                });
                log.info("처리 실패, {}번째 재시도", retryCount);
                channel.basicAck(tag, false);
            }
        }
    }

    /**
     * RabbitMQ는 NACK 후 requeue될 때마다 'x-death' 헤더에 재시도 횟수 (requeue된 횟수)를 추가
     * 해당 헤더를 확인하여 몇번째 재시도 중인지 확인 가능
     * 재시도 횟수가 3회 이상이라면 실패 처리하고 해당 메시지 폐기
     * */
    private int getRetryCount(Message message) {
        MessageProperties properties = message.getMessageProperties();
        Map<String, Object> headers = properties.getHeaders();
        return (int) headers.getOrDefault("x-retry-count", 0);
    }
}
