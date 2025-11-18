package org.pokeherb.hubservice.domain.hubroute.service;

import java.util.List;
import java.util.UUID;

public interface TravelInfoCalculator {
    List<Double> calculateTravelInfo(UUID startHubId, UUID endHubId);
    List<Double> calculateTravelInfo(UUID startHubId, UUID endHubId, List<UUID> routeSequence);
}
