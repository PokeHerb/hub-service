package org.pokeherb.hubservice.infrastructure.messaging.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record OrderStatusRequestMessage(
        UUID orderId,
        String orderStatus,
        LocalDateTime changedAt
) {
}
