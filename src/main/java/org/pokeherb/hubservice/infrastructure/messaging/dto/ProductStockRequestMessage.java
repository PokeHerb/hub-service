package org.pokeherb.hubservice.infrastructure.messaging.dto;

import java.util.UUID;

public record ProductStockRequestMessage(
        UUID orderId,
        Integer quantity
) {
}
