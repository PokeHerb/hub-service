package org.pokeherb.hubservice.application.hubroute.command;

import org.pokeherb.hubservice.application.hubroute.dto.HubRouteCreationRequest;
import org.pokeherb.hubservice.application.hubroute.dto.HubRouteResponse;

public interface HubRouteCommandService {
    // 허브 간 이동 정보 생성
    HubRouteResponse createHubRoute(HubRouteCreationRequest request);
    // 허브 간 이동 정보 수정
    HubRouteResponse updateHubRoute(Long startHubId, Long endHubId);
    // 허브 간 이동 정보 삭제
    void deleteHubRoute(Long startHubId, Long endHubId);
}
