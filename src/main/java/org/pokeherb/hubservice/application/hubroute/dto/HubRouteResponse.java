package org.pokeherb.hubservice.application.hubroute.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import org.pokeherb.hubservice.domain.hubroute.entity.HubRoute;

import java.io.Serializable;

@Schema(description = "허브 간 이동 정보 반환 DTO")
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
