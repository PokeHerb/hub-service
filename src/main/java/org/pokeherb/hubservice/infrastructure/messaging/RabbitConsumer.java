package org.pokeherb.hubservice.infrastructure.messaging;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Service
public class RabbitConsumer {

    @RabbitListener(queues = "hub")
    public void handleMessage(Message message) {
        String routingKey = message.getMessageProperties().getReceivedRoutingKey();
        String payload = new String(message.getBody(), StandardCharsets.UTF_8);

        switch (routingKey) {
            case "hub.created.order":
                // 주문 생성 이벤트 처리
                System.out.println(payload);
        }
    }
}
