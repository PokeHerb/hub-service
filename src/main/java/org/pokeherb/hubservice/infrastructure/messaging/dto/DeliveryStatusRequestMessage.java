package org.pokeherb.hubservice.infrastructure.messaging.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record DeliveryStatusRequestMessage(
        UUID orderId,
        String deliveryStatus,
        LocalDateTime changedAt
) {
}
