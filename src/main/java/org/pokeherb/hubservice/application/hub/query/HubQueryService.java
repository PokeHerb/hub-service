package org.pokeherb.hubservice.application.hub.query;

import org.pokeherb.hubservice.application.hub.dto.HubResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface HubQueryService {
    // 허브 목록 조회
    Page<HubResponse> getHubList(Pageable pageable);
    // 단일 허브 조회
    HubResponse getHub(Long hubId);
    // 허브 검색
    Page<HubResponse> searchHubList(String keyword, Pageable pageable);
    // 허브 존재 확인
    Boolean existsHub(Long id);
}
