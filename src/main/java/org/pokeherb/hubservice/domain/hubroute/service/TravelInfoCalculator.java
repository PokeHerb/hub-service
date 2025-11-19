package org.pokeherb.hubservice.domain.hubroute.service;

import java.util.List;

public interface TravelInfoCalculator {
    List<Double> calculateTravelInfo(Long startHubId, Long endHubId);
}
