package org.pokeherb.hubservice.domain.routing.service;

import java.util.List;
import java.util.UUID;

public interface TravelInfoCalculator {
    List<Double> calculateTravelInfo(UUID startHubId, UUID endHubId);
    List<Double> calculateTravelInfo(UUID startHubId, UUID endHubId, List<UUID> routeSequence);
}
