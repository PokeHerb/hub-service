package org.pokeherb.hubservice.application.cache;

import org.pokeherb.hubservice.domain.hub.entity.Hub;
import org.pokeherb.hubservice.domain.hubroute.entity.HubRoute;

import java.util.List;

public interface CacheService {
    // 허브 캐시
    void putHub(Hub hub);
    void evictHub(Long hubId);

    // 허브 간 이동 정보 캐시
    void putHubRoute(HubRoute hubRoute);
    void evictHubRoute(Long startHubId, Long endHubId);

    // 최종 이동 경로 캐시
    void putFinalRoute(List<Long> hubRouteIds);
    void evictFinalRoute(Long hubId);
}
