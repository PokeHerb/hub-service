package org.pokeherb.hubservice.domain.hubroute.service;

import org.pokeherb.hubservice.application.hub.dto.HubResponse;

import java.util.List;
import java.util.Map;

public interface TravelInfoCalculator {
    Map<String, Double> calculateTravelInfo(Long startHubId, Long endHubId);

    Map<String, Double> calculateFinalTravelInfo(List<HubResponse> routeSequence, Map<String, String> destination);
}
