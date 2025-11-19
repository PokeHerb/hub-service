package org.pokeherb.hubservice.application.hub.dto;

import org.pokeherb.hubservice.domain.hub.entity.Hub;

public record HubResponse(
        Long hubId,
        String hubName,
        String hubAddress,
        Double latitude,
        Double longitude
) {
    public static HubResponse from(Hub hub) {
        return new HubResponse(
                hub.getHubId(),
                hub.getHubName(),
                hub.getAddress().toString(),
                hub.getCoordinate().getLatitude(),
                hub.getCoordinate().getLongitude()
        );
    }
}
