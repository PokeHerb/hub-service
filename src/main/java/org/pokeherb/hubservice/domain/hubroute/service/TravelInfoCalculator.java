package org.pokeherb.hubservice.domain.hubroute.service;

import java.util.Map;

public interface TravelInfoCalculator {
    Map<String, Double> calculateTravelInfo(Long startHubId, Long endHubId);
}
