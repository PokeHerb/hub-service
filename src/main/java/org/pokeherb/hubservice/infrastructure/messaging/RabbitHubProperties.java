package org.pokeherb.hubservice.infrastructure.messaging;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "rabbit.hub")
public record RabbitHubProperties(
        String exchange,
        String queue,
        String routingKey
) {
}
