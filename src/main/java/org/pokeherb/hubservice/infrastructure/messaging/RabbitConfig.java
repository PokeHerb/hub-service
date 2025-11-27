package org.pokeherb.hubservice.infrastructure.messaging;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(RabbitHubProperties.class)
public class RabbitConfig {
    private final RabbitHubProperties hubProperties;

    public static final String DLX = "hub.dlx";
    public static final String DLQ = "hub.failed";
    public static final String RETRY = "hub.retry";
    public static final String RETRY_ROUTING_KEY = "retry.letter";
    public static final String DLQ_ROUTING_KEY = "dead.letter";

    @Bean
    public TopicExchange hubExchange() {
        return new TopicExchange(hubProperties.exchange(), true, false);
    }

    @Bean
    public Queue hubQueue() {
        return QueueBuilder.durable(hubProperties.queue())
                // hubQueue의 DLX 기능 활성화
                .withArgument("x-dead-letter-exchange", DLX)
                .withArgument("x-dead-letter-routing-key", RETRY_ROUTING_KEY)
                .build();
    }

    @Bean
    public Binding hubBinding(Queue hubQueue, TopicExchange hubExchange) {
        return BindingBuilder.bind(hubQueue).to(hubExchange)
                .with(hubProperties.routingKey());
    }

    /**
     * 처리 실패된 메세지를 관리하기 위한 DLX(Dead Letter Exchange)
     * */
    @Bean
    public DirectExchange dlx() {
        return new DirectExchange(DLX);
    }

    /**
     * 재시도를 위해 잠시 메시지를 보관하기 위한 Queue
     * */
    @Bean
    public Queue retryQueue() {
        return QueueBuilder.durable(RETRY)
                .withArgument("x-message-ttl", 3000)
                .withArgument("x-dead-letter-exchange", hubProperties.exchange())
                .withArgument("x-dead-letter-routing-key", hubProperties.routingKey())
                .build();
    }

    /**
     * 실패한 메세지를 저장하는 DLQ(Dead Letter Queue)
     * */
    @Bean
    public Queue dlq() {
        return new Queue(DLQ);
    }

    /**
     * DLX로 들어온 메시지가 RetryQueue로 전달되도록 바인딩
     * */
    @Bean
    public Binding dlxRetryBinding() {
        return BindingBuilder.bind(retryQueue()).to(dlx()).with(RETRY_ROUTING_KEY);
    }

    /**
     * 최종 실패한 메시지가 DLQ로 전달되도록 바인딩
     * */
    @Bean
    public Binding dlxBinding() {
        return BindingBuilder.bind(dlq()).to(dlx()).with(DLQ_ROUTING_KEY);
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);

        factory.setAcknowledgeMode(AcknowledgeMode.MANUAL);

        return factory;
    }
}
