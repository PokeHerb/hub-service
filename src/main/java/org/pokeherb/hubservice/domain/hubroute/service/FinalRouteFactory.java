package org.pokeherb.hubservice.domain.hubroute.service;

import java.util.List;

public interface FinalRouteFactory {
    List<Long> getRouteSequence(Long startHubId, Long endHubId, String cost);
}
