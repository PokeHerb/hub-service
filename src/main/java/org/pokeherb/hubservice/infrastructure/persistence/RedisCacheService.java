package org.pokeherb.hubservice.infrastructure.persistence;

import lombok.RequiredArgsConstructor;
import org.pokeherb.hubservice.application.cache.CacheService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class RedisCacheService implements CacheService {

    private final RedisTemplate<String, Object> redisTemplate;

    private static final String HUB_CACHE = "hubCache";
    private static final String HUB_ROUTE_CACHE = "hubRouteCache";
    private static final String FINAL_ROUTE_CACHE = "finalRouteCache";
    private static final String FINAL_ROUTE_HUB_INDEX = "finalRouteHubIndex";

    @Override
    public void evictHub(Long hubId) {
        redisTemplate.delete(HUB_CACHE + "::" + hubId);
    }

    @Override
    public void evictHubRoute(Long startHubId, Long endHubId) {
        redisTemplate.delete(HUB_ROUTE_CACHE + "::" + startHubId + "::" + endHubId);
    }

    @Override
    public void putFinalRoute(List<Long> hubIds, String routeKey) {
        if (hubIds == null || hubIds.isEmpty() || routeKey == null) return;

        // 최종 이동 경로로 set 인덱스 키 생성
        String hubSetKey = FINAL_ROUTE_HUB_INDEX + "::" + hubIds.stream()
                .sorted()
                .map(String::valueOf)
                .collect(Collectors.joining("-"));

        // 경로 집합을 key로 하여 해당 경로 집합을 갖는 최종 경로 키를 저장 (삭제 시 사용)
        redisTemplate.opsForSet().add(hubSetKey, routeKey);
    }

    @Override
    public void evictFinalRoute(Long hubId) {
        Set<String> allHubSetKeys = redisTemplate.keys(FINAL_ROUTE_HUB_INDEX + "::" + "*");

        if (allHubSetKeys.isEmpty()) return;

        for (String hubSetKey : allHubSetKeys) {
            // 집합 인덱스 키에서 특정 hubId를 갖고 있는지 확인
            List<Long> hubIdsInKey = parseHubSetKey(hubSetKey);
            if (!hubIdsInKey.contains(hubId)) continue;

            // 특정 hubId를 갖고 있는 인덱스 키가 있을 경우 보유하고 있는 routeKey들을 가져옴
            Set<Object> routeKeys = redisTemplate.opsForSet().members(hubSetKey);
            if (routeKeys != null && !routeKeys.isEmpty()) {
                for (Object routeKeyObj : routeKeys) {
                    String routeKey = (String) routeKeyObj;
                    // 해당 routeKey를 삭제
                    redisTemplate.delete(FINAL_ROUTE_CACHE + "::" + routeKey);
                }
            }

            // 삭제 완료 후 해당 인덱스 키도 삭제
            redisTemplate.delete(hubSetKey);
        }
    }

    private List<Long> parseHubSetKey(String hubSetKey) {
        String suffix = hubSetKey.substring(hubSetKey.indexOf("::") + 2);
        String[] parts = suffix.split("-");
        List<Long> hubIds = new ArrayList<>();
        for (String part : parts) {
            hubIds.add(Long.parseLong(part));
        }
        return hubIds;
    }
}
