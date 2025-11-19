package org.pokeherb.hubservice.application.hubroute.dto;

import org.pokeherb.hubservice.domain.hubroute.entity.HubRoute;

public record HubRouteResponse(
        Long hubRouteId,
        Long startHubId,
        Long endHubId,
        Double duration,
        Double distance
) {
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
