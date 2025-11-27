package org.pokeherb.hubservice.infrastructure.messaging.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record OrderCreatedEventMessage(
        UUID orderId,
        UUID productId,
        String orderStatus,
        LocalDateTime dueAt,
        String requestMemo,
        Long startHubId,
        Long endHubId,
        Integer quantity,
        UUID orderUserId,
        String productName,
        UUID requestVendorId,
        UUID receiveVendorId,
        String vendorAddress,
        UUID receiverSlackId,
        String receiverName
) {
}
