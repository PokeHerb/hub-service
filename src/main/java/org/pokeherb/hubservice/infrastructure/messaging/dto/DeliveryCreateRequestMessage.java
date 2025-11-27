package org.pokeherb.hubservice.infrastructure.messaging.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record DeliveryCreateRequestMessage(
        UUID orderId,
        List<Long> sequence,
        Long startHubId,
        Long endHubId,
        UUID endVendorId,
        String endVendorAddress,
        UUID receiverSlackId,
        String receiverName,
        Double finalDuration,
        Double finalDistance,
        UUID productId,
        LocalDateTime dueAt,
        UUID orderUserId,
        String productName,
        UUID driverId
) {
    public static DeliveryCreateRequestMessage from(OrderCreatedEventMessage eventMessage, List<Long> routeSequence, Double finalDuration, Double finalDistance, UUID driverId) {
        return new DeliveryCreateRequestMessage(
                eventMessage.orderId(),
                routeSequence,
                eventMessage.startHubId(),
                eventMessage.endHubId(),
                eventMessage.receiveVendorId(),
                eventMessage.vendorAddress(),
                eventMessage.receiverSlackId(),
                eventMessage.receiverName(),
                finalDuration,
                finalDistance,
                eventMessage.productId(),
                eventMessage.dueAt(),
                eventMessage.orderUserId(),
                eventMessage.productName(),
                driverId
        );
    }
}
