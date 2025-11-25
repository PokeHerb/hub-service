package org.pokeherb.hubservice.application.finalroute.service;

import lombok.RequiredArgsConstructor;
import org.pokeherb.hubservice.application.hub.dto.HubResponse;
import org.pokeherb.hubservice.domain.hub.entity.Hub;
import org.pokeherb.hubservice.domain.hub.repository.HubRepository;
import org.pokeherb.hubservice.domain.hubroute.service.FinalRouteFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FinalRouteServiceImpl implements FinalRouteService {

    private final FinalRouteFactory finalRouteFactory;
    private final HubRepository hubRepository;

    @Override
    @Cacheable(
            cacheNames = "finalRouteCache",
            key = "T(String).valueOf(#startHubId) + '::' + T(String).valueOf(#endHubId) + '::' + T(String).valueOf(#cost)"
    )
    public List<HubResponse> getFinalHubRoute(Long startHubId, Long endHubId, String cost) {
        List<Long> routeSequence = finalRouteFactory.getRouteSequence(startHubId, endHubId, cost);

        List<Hub> hubs = hubRepository.findByHubIdInAndDeletedAtIsNull(routeSequence);
        Map<Long, Hub> hubMap = hubs.stream()
                .collect(Collectors.toMap(Hub::getHubId, Function.identity()));

        return routeSequence.stream()
                .map(hubMap::get)
                .map(HubResponse::from)
                .toList();
    }
}
