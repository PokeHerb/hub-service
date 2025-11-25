package org.pokeherb.hubservice.application.finalroute.service;

import org.pokeherb.hubservice.application.hub.dto.HubResponse;

import java.util.List;

public interface FinalRouteService {
    List<HubResponse> getFinalHubRoute(Long startHubId, Long endHubId, String cost);
}
