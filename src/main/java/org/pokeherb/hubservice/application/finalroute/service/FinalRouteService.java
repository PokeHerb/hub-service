package org.pokeherb.hubservice.application.finalroute.service;

import org.pokeherb.hubservice.application.finalroute.dto.FinalRouteResponse;
import org.pokeherb.hubservice.application.hub.dto.HubResponse;

import java.util.List;
import java.util.Map;

public interface FinalRouteService {
    List<HubResponse> getFinalHubRoute(Long startHubId, Long endHubId, String cost);
    FinalRouteResponse getDeliveryInfo(List<HubResponse> routeSequence, Map<String, Double> destination);
}
