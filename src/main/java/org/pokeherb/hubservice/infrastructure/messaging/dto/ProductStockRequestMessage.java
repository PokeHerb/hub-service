package org.pokeherb.hubservice.infrastructure.messaging.dto;

import java.util.UUID;

public record ProductStockRequestMessage(
        UUID productId,
        int quantity
) {
}
