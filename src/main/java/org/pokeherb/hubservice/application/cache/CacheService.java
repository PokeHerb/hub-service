package org.pokeherb.hubservice.application.cache;

import java.util.List;

public interface CacheService {
    // 허브 캐시
    void evictHub(Long hubId);

    // 허브 간 이동 정보 캐시
    void evictHubRoute(Long startHubId, Long endHubId);

    // 최종 이동 경로 캐시
    void putFinalRoute(List<Long> hubIds, String routeKey);
    void evictFinalRoute(Long hubId);
}
