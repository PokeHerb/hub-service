package org.pokeherb.hubservice.infrastructure.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.pokeherb.hubservice.global.infrastructure.exception.CustomException;
import org.pokeherb.hubservice.infrastructure.exception.RabbitErrorCode;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
public class RabbitProducer {

    private final RabbitTemplate template;
    private final ObjectMapper objectMapper;

    public void publishEvent(Object payload, String routingKey) {
        sendMessage(routingKey, payload);
    }

    private void sendMessage(String routingKey, Object payload) {
        try {
            String json = objectMapper.writeValueAsString(payload);
            MessageProperties messageProperties = new MessageProperties();
            messageProperties.setContentType(MessageProperties.CONTENT_TYPE_JSON);
            Message message = new Message(json.getBytes(StandardCharsets.UTF_8), messageProperties);

            template.convertAndSend("pokeherb", routingKey, message);
        } catch (JsonProcessingException e) {
            throw new CustomException(RabbitErrorCode.RABBIT_JSON_PROCESSING_FAILED);
        }
    }
}
