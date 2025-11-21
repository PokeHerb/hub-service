package org.pokeherb.hubservice.application.hubroute.query;

import lombok.RequiredArgsConstructor;
import org.pokeherb.hubservice.application.hubroute.dto.HubRouteResponse;
import org.pokeherb.hubservice.domain.hubroute.entity.HubRoute;
import org.pokeherb.hubservice.domain.hubroute.exception.HubRouteErrorCode;
import org.pokeherb.hubservice.domain.hubroute.repository.HubRouteDetailsRepository;
import org.pokeherb.hubservice.domain.hubroute.repository.HubRouteRepository;
import org.pokeherb.hubservice.global.infrastructure.exception.CustomException;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HubRouteQueryServiceImpl implements HubRouteQueryService {

    private final HubRouteRepository hubRouteRepository;
    private final HubRouteDetailsRepository hubRouteDetailsRepository;

    @Override
    public List<HubRouteResponse> getHubRouteList() {
        List<HubRoute> hubRoutes = hubRouteRepository.findAllByDeletedAtIsNull();
        return hubRoutes.stream().map(HubRouteResponse::from).toList();
    }

    @Override
    @Cacheable(
            value = "hubRouteCache",
            key = "T(String).valueOf(#startHubId) + '::' + T(String).valueOf(#endHubId)")
    public HubRouteResponse getHubRoute(Long startHubId, Long endHubId) {
        HubRoute hubRoute = hubRouteRepository.findByStartHubIdAndEndHubId(startHubId, endHubId).orElseThrow(() -> new CustomException(HubRouteErrorCode.HUB_ROUTE_NOT_FOUND));
        return HubRouteResponse.from(hubRoute);
    }

    @Override
    public List<HubRouteResponse> searchHubRouteList(String keyword) {
        List<HubRoute> hubRoutes = hubRouteDetailsRepository.searchHubRouteByKeyword(keyword);
        return hubRoutes.stream().map(HubRouteResponse::from).toList();
    }
}
