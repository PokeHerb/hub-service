package org.pokeherb.hubservice.infrastructure.messaging;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.*;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(RabbitHubProperties.class)
public class RabbitConfig {
    private final RabbitHubProperties hubProperties;

    @Bean
    public TopicExchange hubExchange() {
        return new TopicExchange(hubProperties.exchange(), true, false);
    }

    @Bean
    public Queue hubQueue() {
        return QueueBuilder.durable(hubProperties.queue()).build();
    }

    @Bean
    public Binding hubBinding(Queue hubQueue, TopicExchange hubExchange) {
        return BindingBuilder.bind(hubQueue).to(hubExchange)
                .with(hubProperties.routingKey());
    }
}
