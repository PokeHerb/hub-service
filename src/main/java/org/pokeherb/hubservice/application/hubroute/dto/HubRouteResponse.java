package org.pokeherb.hubservice.application.hubroute.dto;

import org.pokeherb.hubservice.domain.hubroute.entity.HubRoute;

import java.io.Serializable;

public record HubRouteResponse(
        Long hubRouteId,
        Long startHubId,
        Long endHubId,
        Double duration,
        Double distance
) implements Serializable {
    public static HubRouteResponse from(HubRoute hubRoute) {
        return new HubRouteResponse(
                hubRoute.getHubRouteId(),
                hubRoute.getStartHubId(),
                hubRoute.getEndHubId(),
                hubRoute.getTravelInfo().getDuration(),
                hubRoute.getTravelInfo().getDistance()
        );
    }
}
