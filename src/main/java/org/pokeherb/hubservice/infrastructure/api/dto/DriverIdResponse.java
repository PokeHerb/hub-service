package org.pokeherb.hubservice.infrastructure.api.dto;

import java.util.UUID;

public record DriverIdResponse(
        UUID driverId,
        String driverName
) {
}
