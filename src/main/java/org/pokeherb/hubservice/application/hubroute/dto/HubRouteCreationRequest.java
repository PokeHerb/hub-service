package org.pokeherb.hubservice.application.hubroute.dto;

public record HubRouteCreationRequest(
        Long startHubId,
        Long endHubId
) {
}
