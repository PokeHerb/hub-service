package org.pokeherb.hubservice.infrastructure.messaging;

import org.junit.jupiter.api.Test;
import org.pokeherb.hubservice.application.hub.dto.HubResponse;
import org.pokeherb.hubservice.domain.hub.repository.HubRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Objects;

@SpringBootTest
public class RabbitmqTest {

    @Autowired
    private RabbitProducer rabbitProducer;
    @Autowired
    private HubRepository hubRepository;

    @Test
    void rabbitmqTest() {
        HubResponse hub = HubResponse.from(Objects.requireNonNull(hubRepository.findByHubIdAndDeletedAtIsNull(1L).orElse(null)));
        rabbitProducer.publishDeliveryEvent(hub, "hub.created.order");
    }
}
