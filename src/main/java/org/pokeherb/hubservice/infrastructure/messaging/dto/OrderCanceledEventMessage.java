package org.pokeherb.hubservice.infrastructure.messaging.dto;

import java.util.UUID;

public record OrderCanceledEventMessage(
        UUID orderId,
        Integer quantity
) {
}
