package org.pokeherb.hubservice.infrastructure.api.dto;

import java.util.UUID;

/**
 * 내부적으로 driver-service와 통신하기 위한 DTO
 * */
public record DriverIdResponse(
        UUID driverId,
        String driverName
) {
}
