package org.pokeherb.hubservice.infrastructure.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface MessageEventHandler {
    void handleOrderCreatedEvent(String payload) throws JsonProcessingException;
    void handleOrderCanceledEvent(String payload) throws JsonProcessingException;
}
