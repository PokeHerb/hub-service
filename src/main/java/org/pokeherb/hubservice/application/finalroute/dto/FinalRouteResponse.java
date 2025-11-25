package org.pokeherb.hubservice.application.finalroute.dto;

import org.pokeherb.hubservice.application.hub.dto.HubResponse;

import java.util.List;

public record FinalRouteResponse(
        Double finalDistance,
        Double finalDuration,
        List<HubResponse> routeSequence
) {
}
