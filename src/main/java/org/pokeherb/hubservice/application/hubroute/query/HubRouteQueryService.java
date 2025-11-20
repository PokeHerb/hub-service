package org.pokeherb.hubservice.application.hubroute.query;

import org.pokeherb.hubservice.application.hubroute.dto.HubRouteResponse;

import java.util.List;

public interface HubRouteQueryService {
    // 허브 간 이동 정보 목록 조회
    List<HubRouteResponse> getHubRouteList();
    // 허브 간 이동 정보 단일 조회 (출발지 허브, 목적지 허브 지정)
    HubRouteResponse getHubRoute(Long startHubId, Long endHubId);
    // 허브 간 이동 정보 검색
    List<HubRouteResponse> searchHubRouteList(String keyword);
}
