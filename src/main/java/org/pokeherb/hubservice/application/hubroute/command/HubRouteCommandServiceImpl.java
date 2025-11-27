package org.pokeherb.hubservice.application.hubroute.command;

import lombok.RequiredArgsConstructor;
import org.pokeherb.hubservice.application.hubroute.dto.HubRouteCreationRequest;
import org.pokeherb.hubservice.application.hubroute.dto.HubRouteResponse;
import org.pokeherb.hubservice.domain.hub.entity.Hub;
import org.pokeherb.hubservice.domain.hub.exception.HubErrorCode;
import org.pokeherb.hubservice.domain.hub.repository.HubRepository;
import org.pokeherb.hubservice.domain.hub.service.CheckAccessHub;
import org.pokeherb.hubservice.domain.hubroute.entity.HubRoute;
import org.pokeherb.hubservice.domain.hubroute.exception.HubRouteErrorCode;
import org.pokeherb.hubservice.domain.hubroute.repository.HubRouteRepository;
import org.pokeherb.hubservice.domain.hubroute.service.TravelInfoCalculator;
import org.pokeherb.hubservice.global.infrastructure.exception.CustomException;
import org.pokeherb.hubservice.infrastructure.security.SecurityUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class HubRouteCommandServiceImpl implements HubRouteCommandService {

    private final HubRouteRepository hubRouteRepository;
    private final HubRepository hubRepository;
    private final CheckAccessHub checkAccessHub;
    private final TravelInfoCalculator travelInfoCalculator;
    private final SecurityUtils securityUtils;

    /**
     * 허브 간 이동 정보 생성 (연결된 허브 정보)
     * */
    @Override
    @CachePut(
            cacheNames = "hubRouteCache",
            key = "T(String).valueOf(#result.startHubId) + '::' + T(String).valueOf(#result.endHubId)"
    )
    public HubRouteResponse createHubRoute(HubRouteCreationRequest request) {
        Hub startHub = hubRepository.findByHubIdAndDeletedAtIsNull(request.startHubId()).orElseThrow(() -> new CustomException(HubErrorCode.HUB_NOT_FOUND));
        Hub endHub = hubRepository.findByHubIdAndDeletedAtIsNull(request.endHubId()).orElseThrow(() -> new CustomException(HubErrorCode.HUB_NOT_FOUND));
        HubRoute hubRoute = hubRouteRepository.save(HubRoute.builder()
                .startHubId(startHub.getHubId())
                .endHubId(endHub.getHubId())
                .calculator(travelInfoCalculator)
                .checkAccessHub(checkAccessHub)
                .build());
        return HubRouteResponse.from(hubRoute);
    }

    /**
     * 허브 간 이동 정보 업데이트 (소요시간/이동거리)
     * */
    @Override
    @CachePut(
            cacheNames = "hubRouteCache",
            key = "T(String).valueOf(#result.startHubId) + '::' + T(String).valueOf(#result.endHubId)"
    )
    public HubRouteResponse updateHubRoute(Long startHubId, Long endHubId) {
        HubRoute hubRoute = hubRouteRepository.findByStartHubIdAndEndHubIdAndDeletedAtIsNull(startHubId, endHubId).orElseThrow(() -> new CustomException(HubRouteErrorCode.HUB_ROUTE_NOT_FOUND));
        hubRoute.changeTravelInfo(travelInfoCalculator, checkAccessHub);
        return HubRouteResponse.from(hubRouteRepository.save(hubRoute));
    }

    /**
     * 허브 간 이동 정보 삭제
     * - soft delete 처리
     * */
    @Override
    @CacheEvict(
            cacheNames = "hubRouteCache",
            key = "T(String).valueOf(#result.startHubId) + '::' + T(String).valueOf(#result.endHubId)"
    )
    public void deleteHubRoute(Long startHubId, Long endHubId) {
        HubRoute hubRoute = hubRouteRepository.findByStartHubIdAndEndHubIdAndDeletedAtIsNull(startHubId, endHubId).orElseThrow(() -> new CustomException(HubRouteErrorCode.HUB_ROUTE_NOT_FOUND));
        // 현재 로그인한 사용자 username 가져오기
        String username = securityUtils.getCurrentUsername();
        hubRoute.deleteHubRoute(username, checkAccessHub);
    }
}
