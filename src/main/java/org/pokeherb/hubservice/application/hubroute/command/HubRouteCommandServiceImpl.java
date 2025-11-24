package org.pokeherb.hubservice.application.hubroute.command;

import lombok.RequiredArgsConstructor;
import org.pokeherb.hubservice.application.cache.CacheService;
import org.pokeherb.hubservice.application.hubroute.dto.HubRouteCreationRequest;
import org.pokeherb.hubservice.application.hubroute.dto.HubRouteResponse;
import org.pokeherb.hubservice.domain.hub.service.CheckAccessHub;
import org.pokeherb.hubservice.domain.hubroute.entity.HubRoute;
import org.pokeherb.hubservice.domain.hubroute.exception.HubRouteErrorCode;
import org.pokeherb.hubservice.domain.hubroute.repository.HubRouteRepository;
import org.pokeherb.hubservice.domain.hubroute.service.TravelInfoCalculator;
import org.pokeherb.hubservice.global.infrastructure.exception.CustomException;
import org.pokeherb.hubservice.infrastructure.security.SecurityUtils;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class HubRouteCommandServiceImpl implements HubRouteCommandService {

    private final HubRouteRepository hubRouteRepository;
    private final CheckAccessHub checkAccessHub;
    private final TravelInfoCalculator travelInfoCalculator;
    private final SecurityUtils securityUtils;
    private final CacheService cacheService;

    @Override
    @CachePut(
            value = "hubRouteCache",
            key = "T(String).valueOf(#result.startHubId) + '::' + T(String).valueOf(#result.endHubId)")
    public HubRouteResponse createHubRoute(HubRouteCreationRequest request) {
        HubRoute hubRoute = hubRouteRepository.save(HubRoute.builder()
                .startHubId(request.startHubId())
                .endHubId(request.endHubId())
                .calculator(travelInfoCalculator)
                .checkAccessHub(checkAccessHub)
                .build());
        return HubRouteResponse.from(hubRoute);
    }

    @Override
    @CachePut(
            value = "hubRouteCache",
            key = "T(String).valueOf(#result.startHubId) + '::' + T(String).valueOf(#result.endHubId)")
    public HubRouteResponse updateHubRoute(Long startHubId, Long endHubId) {
        HubRoute hubRoute = hubRouteRepository.findByStartHubIdAndEndHubIdAndDeletedAtIsNull(startHubId, endHubId).orElseThrow(() -> new CustomException(HubRouteErrorCode.HUB_ROUTE_NOT_FOUND));
        hubRoute.changeTravelInfo(travelInfoCalculator, checkAccessHub);
        return HubRouteResponse.from(hubRouteRepository.save(hubRoute));
    }

    @Override
    public void deleteHubRoute(Long startHubId, Long endHubId) {
        HubRoute hubRoute = hubRouteRepository.findByStartHubIdAndEndHubIdAndDeletedAtIsNull(startHubId, endHubId).orElseThrow(() -> new CustomException(HubRouteErrorCode.HUB_ROUTE_NOT_FOUND));
        // 현재 로그인한 사용자 username 가져오기
        String username = securityUtils.getCurrentUsername();
        hubRoute.deleteHubRoute(username, checkAccessHub);
        // 캐시도 삭제
        cacheService.evictHubRoute(startHubId, endHubId);
        // 연관된 최종 이동 경로 삭제
        cacheService.evictFinalRoute(startHubId);
        cacheService.evictFinalRoute(endHubId);
    }
}
