package org.pokeherb.hubservice.infrastructure.messaging;

import org.junit.jupiter.api.Test;
import org.pokeherb.hubservice.infrastructure.messaging.dto.OrderCreatedEventMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.UUID;

@SpringBootTest
public class RabbitmqTest {

    @Autowired
    private RabbitProducer rabbitProducer;

    @Test
    void rabbitmqOrderCreatedEventTest() {
        OrderCreatedEventMessage requestMessage = new OrderCreatedEventMessage(
                UUID.randomUUID(),
                UUID.randomUUID(),
                "주문 생성",
                LocalDateTime.now(),
                "test",
                1L,
                4L,
                3,
                UUID.randomUUID(),
                "상품",
                UUID.randomUUID(),
                UUID.randomUUID(),
                "부산광역시 해운대구 해운대해변로 266",
                UUID.randomUUID(),
                "수령인",
                LocalDateTime.now()
        );
        rabbitProducer.publishEvent(requestMessage, "hub.created.order");
    }

    @Test
    void rabbitmqTest() {
        rabbitProducer.publishEvent("test", "hub.retry.test");
    }
}
