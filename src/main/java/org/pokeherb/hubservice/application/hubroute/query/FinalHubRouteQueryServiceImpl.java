package org.pokeherb.hubservice.application.hubroute.query;

import lombok.RequiredArgsConstructor;
import org.pokeherb.hubservice.application.cache.CacheService;
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
public class FinalHubRouteQueryServiceImpl implements FinalHubRouteQueryService {

    private final FinalRouteFactory finalRouteFactory;
    private final HubRepository hubRepository;
    private final CacheService cacheService;

    @Override
    @Cacheable(
            value = "finalRouteCache",
            key = "T(String).valueOf(#startHubId) + '::' + T(String).valueOf(#endHubId)"
    )
    public List<HubResponse> getFinalHubRoute(Long startHubId, Long endHubId) {
        List<Long> routeSequence = finalRouteFactory.getRouteSequence(startHubId, endHubId);
        String routeKey = "finalRouteCache::" + startHubId + "::" + endHubId;

        // 최종 경로 캐시 인덱스 생성
        cacheService.putFinalRoute(routeSequence, routeKey);

        List<Hub> hubs = hubRepository.findByHubIdInAndDeletedAtIsNull(routeSequence);
        Map<Long, Hub> hubMap = hubs.stream()
                .collect(Collectors.toMap(Hub::getHubId, Function.identity()));

        return routeSequence.stream()
                .map(hubMap::get)
                .map(HubResponse::from)
                .toList();
    }
}
