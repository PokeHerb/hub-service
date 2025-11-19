package org.pokeherb.hubservice.domain.hub.service;

import lombok.RequiredArgsConstructor;
import org.pokeherb.hubservice.domain.hub.entity.Hub;
import org.pokeherb.hubservice.domain.hubroute.entity.HubRoute;
import org.pokeherb.hubservice.domain.hubroute.repository.HubRouteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class HubDeletionServiceImpl implements HubDeletionService {

    private final CheckAccessHub checkAccessHub;
    private final HubRouteRepository hubRouteRepository;

    @Override
    public void deleteHub(Hub hub, String userName) {
        // 허브 삭제
        hub.deleteHub(userName, checkAccessHub);

        // 삭제된 허브가 포함된 허브 간 이동 정보도 삭제 (비활성화)
        List<HubRoute> hubRoutes = hubRouteRepository.findByStartHubIdOrEndHubId(hub.getHubId(), hub.getHubId());
        hubRoutes.forEach(hubRoute -> {
            hubRoute.deleteHubRoute(userName, checkAccessHub);
        });
    }
}
