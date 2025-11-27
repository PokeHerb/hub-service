package org.pokeherb.hubservice.application.hubroute.query;

import lombok.RequiredArgsConstructor;
import org.pokeherb.hubservice.application.hubroute.dto.HubRouteResponse;
import org.pokeherb.hubservice.domain.hubroute.entity.HubRoute;
import org.pokeherb.hubservice.domain.hubroute.exception.HubRouteErrorCode;
import org.pokeherb.hubservice.domain.hubroute.repository.HubRouteDetailsRepository;
import org.pokeherb.hubservice.domain.hubroute.repository.HubRouteRepository;
import org.pokeherb.hubservice.global.infrastructure.exception.CustomException;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HubRouteQueryServiceImpl implements HubRouteQueryService {

    private final HubRouteRepository hubRouteRepository;
    private final HubRouteDetailsRepository hubRouteDetailsRepository;

    /**
     * soft delete되지 않은 모든 허브 간 이동 정보 목록 조회
     * - pagination
     * */
    @Override
    public Page<HubRouteResponse> getHubRouteList(Pageable pageable) {
        Page<HubRoute> hubRoutes = hubRouteRepository.findAllByDeletedAtIsNull(pageable);
        return hubRoutes.map(HubRouteResponse::from);
    }

    /**
     * 출발 허브 id, 도착 허브 id를 통해 허브 간 이동 정보 조회
     * */
    @Override
    @Cacheable(
            cacheNames = "hubRouteCache",
            key = "T(String).valueOf(#startHubId) + '::' + T(String).valueOf(#endHubId)"
    )
    public HubRouteResponse getHubRoute(Long startHubId, Long endHubId) {
        HubRoute hubRoute = hubRouteRepository.findByStartHubIdAndEndHubIdAndDeletedAtIsNull(startHubId, endHubId).orElseThrow(() -> new CustomException(HubRouteErrorCode.HUB_ROUTE_NOT_FOUND));
        return HubRouteResponse.from(hubRoute);
    }

    /**
     * keyword를 통해 허브 간 이동 정보 검색
     * - pagination
     * */
    @Override
    public Page<HubRouteResponse> searchHubRouteList(String keyword, Pageable pageable) {
        Page<HubRoute> hubRoutes = hubRouteDetailsRepository.searchHubRouteByKeyword(keyword, pageable);
        return hubRoutes.map(HubRouteResponse::from);
    }
}
