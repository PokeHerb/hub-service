package org.pokeherb.hubservice.application.hub.command;

import lombok.RequiredArgsConstructor;
import org.pokeherb.hubservice.application.cache.CacheService;
import org.pokeherb.hubservice.application.hub.dto.HubCreationRequest;
import org.pokeherb.hubservice.application.hub.dto.HubModificationRequest;
import org.pokeherb.hubservice.application.hub.dto.HubResponse;
import org.pokeherb.hubservice.domain.hub.entity.Hub;
import org.pokeherb.hubservice.domain.hub.exception.HubErrorCode;
import org.pokeherb.hubservice.domain.hub.repository.HubRepository;
import org.pokeherb.hubservice.domain.hub.service.AddressToCoordinateConverter;
import org.pokeherb.hubservice.domain.hub.service.CheckAccessHub;
import org.pokeherb.hubservice.domain.hubroute.entity.HubRoute;
import org.pokeherb.hubservice.domain.hubroute.repository.HubRouteRepository;
import org.pokeherb.hubservice.domain.hubroute.service.TravelInfoCalculator;
import org.pokeherb.hubservice.global.infrastructure.exception.CustomException;
import org.pokeherb.hubservice.infrastructure.security.SecurityUtils;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class HubCommandServiceImpl implements HubCommandService {

    private final HubRepository hubRepository;
    private final HubRouteRepository hubRouteRepository;
    private final CheckAccessHub checkAccessHub;
    private final AddressToCoordinateConverter addressToCoordinateConverter;
    private final TravelInfoCalculator travelInfoCalculator;
    private final SecurityUtils securityUtils;
    private final CacheService cacheService;

    @Override
    @CachePut(value = "hubCache", key = "#result.hubId")
    public HubResponse createHub(HubCreationRequest request) {
        Hub hub = hubRepository.save(Hub.builder()
                .hubName(request.hubName())
                .sido(request.address().sido())
                .sigungu(request.address().sigungu())
                .eupmyeon(request.address().eupmyeon())
                .ri(request.address().ri())
                .dong(request.address().dong())
                .street(request.address().street())
                .buildingNo(request.address().buildingNo())
                .details(request.address().details())
                .build());
        return HubResponse.from(hub);
    }

    @Override
    @CachePut(value = "hubCache", key = "#result.hubId")
    public HubResponse modifyHub(Long hubId, HubModificationRequest request) {
        Hub hub = hubRepository.findByHubId(hubId).orElseThrow(() -> new CustomException(HubErrorCode.HUB_NOT_FOUND));
        if (request.hubName() != null) {
            hub.changeHubName(request.hubName(), checkAccessHub);
        }
        if (request.address() != null) {
            // 허브 주소 수정
            hub.changeAddress(
                    request.address().sido(),
                    request.address().sigungu(),
                    request.address().eupmyeon(),
                    request.address().ri(),
                    request.address().dong(),
                    request.address().street(),
                    request.address().buildingNo(),
                    request.address().details(),
                    addressToCoordinateConverter,
                    checkAccessHub);
            // 수정된 허브가 포함된 허브 간 이동 정보 소요시간, 이동거리 재계산
            List<HubRoute> hubRoutes = hubRouteRepository.findByStartHubIdOrEndHubId(hub.getHubId(),  hub.getHubId());
            hubRoutes.forEach(hubRoute -> {
                hubRoute.changeTravelInfo(travelInfoCalculator, checkAccessHub);
            });
        }
        return HubResponse.from(hubRepository.save(hub));
    }

    @Override
    public void deleteHub(Long hubId) {
        Hub hub = hubRepository.findByHubId(hubId).orElseThrow(() -> new CustomException(HubErrorCode.HUB_NOT_FOUND));

        // 현재 로그인한 사용자 username 가져오기
        String username = securityUtils.getCurrentUsername();
        // 허브 삭제
        hub.deleteHub(username, checkAccessHub);
        // 캐시에서도 허브 삭제
        cacheService.evictHub(hubId);

        // 삭제된 허브가 포함된 허브 간 이동 정보도 삭제 (비활성화)
        List<HubRoute> hubRoutes = hubRouteRepository.findByStartHubIdOrEndHubId(hub.getHubId(), hub.getHubId());
        hubRoutes.forEach(hubRoute -> {
            hubRoute.deleteHubRoute(username, checkAccessHub);
            // 캐시에서도 허브 간 이동 정보 삭제
            cacheService.evictHubRoute(hubRoute.getStartHubId(), hubRoute.getEndHubId());
        });

        // 최종 이동 경로 캐시에서도 해당 허브가 포함된 캐시 삭제
        cacheService.evictFinalRoute(hubId);
    }
}
