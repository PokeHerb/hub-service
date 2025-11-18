package org.pokeherb.hubservice.domain.routing.service;

import java.util.List;
import java.util.UUID;

public interface FinalRouteFactory {
    List<UUID> getRouteSequence(UUID startHubId, UUID endHubId);
}
