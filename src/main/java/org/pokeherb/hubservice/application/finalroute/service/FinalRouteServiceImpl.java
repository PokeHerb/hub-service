package org.pokeherb.hubservice.application.finalroute.service;

import lombok.RequiredArgsConstructor;
import org.pokeherb.hubservice.application.finalroute.dto.FinalRouteResponse;
import org.pokeherb.hubservice.application.hub.dto.HubResponse;
import org.pokeherb.hubservice.domain.hub.entity.Hub;
import org.pokeherb.hubservice.domain.hub.repository.HubRepository;
import org.pokeherb.hubservice.domain.hubroute.service.FinalRouteFactory;
import org.pokeherb.hubservice.domain.hubroute.service.TravelInfoCalculator;
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
    private final TravelInfoCalculator travelInfoCalculator;
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

    /**
     * 배송 정보 생성 요청 시 포함할 업체까지의 최종 소요시간 및 이동거리 계산
     * @return FinalRouteResponse 최종 소요시간, 최종 이동거리, 허브 간 이동 순서 정보
     * */
    public FinalRouteResponse getDeliveryInfo(List<HubResponse> routeSequence, Map<String, String> destination) {
        Map<String, Double> finalTravelInfo = travelInfoCalculator.calculateFinalTravelInfo(routeSequence, destination);
        return new FinalRouteResponse(
                finalTravelInfo.get("finalDistance"),
                finalTravelInfo.get("finalDuration"),
                routeSequence
        );
    }
}
