package org.pokeherb.hubservice.application.hubroute.query;

import org.pokeherb.hubservice.application.hub.dto.HubResponse;

import java.util.List;

public interface FinalHubRouteQueryService {
    List<HubResponse> getFinalHubRoute(Long startHubId, Long endHubId, String cost);
}
