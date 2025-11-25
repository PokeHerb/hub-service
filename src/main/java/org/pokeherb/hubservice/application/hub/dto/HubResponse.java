package org.pokeherb.hubservice.application.hub.dto;

import org.pokeherb.hubservice.domain.hub.entity.Hub;

import java.io.Serializable;

public record HubResponse(
        Long hubId,
        String hubName,
        String hubAddress,
        Double latitude,
        Double longitude
) implements Serializable {
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
