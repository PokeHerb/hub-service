package org.pokeherb.hubservice.domain.hub.service;

import lombok.RequiredArgsConstructor;
import org.pokeherb.hubservice.domain.hub.entity.Hub;
import org.pokeherb.hubservice.domain.hub.repository.HubRepository;
import org.pokeherb.hubservice.domain.hubroute.entity.HubRoute;
import org.pokeherb.hubservice.domain.hubroute.repository.HubRouteRepository;
import org.pokeherb.hubservice.domain.hubroute.service.TravelInfoCalculator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class HubModificationServiceImpl implements HubModificationService {

    private final CheckAccessHub checkAccessHub;
    private final AddressToCoordinateConverter addressToCoordinateConverter;
    private final HubRouteRepository hubRouteRepository;
    private final TravelInfoCalculator travelInfoCalculator;
    private final HubRepository hubRepository;

    @Override
    public Hub modifyHubAddress(Hub hub, String sido, String sigungu, String eupmyeon, String dong, String ri, String street, String building_no, String details) {
        // 허브 주소 수정
        hub.changeAddress(sido, sigungu, eupmyeon, dong, ri, street, building_no, details, addressToCoordinateConverter, checkAccessHub);

        // 수정된 허브가 포함된 허브 간 이동 정보 소요시간, 이동거리 재계산
        List<HubRoute> hubRoutes = hubRouteRepository.findByStartHubIdOrEndHubId(hub.getHubId(),  hub.getHubId());
        hubRoutes.forEach(hubRoute -> {
            hubRoute.changeTravelInfo(travelInfoCalculator, checkAccessHub);
        });
        return hubRepository.save(hub);
    }
}
