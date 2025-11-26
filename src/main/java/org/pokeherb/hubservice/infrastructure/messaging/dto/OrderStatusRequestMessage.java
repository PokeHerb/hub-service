package org.pokeherb.hubservice.infrastructure.messaging.dto;

import java.util.UUID;

public record OrderStatusRequestMessage(
        UUID orderId,
        String orderStatus
) {
}
