package org.pokeherb.hubservice.application.hub.query;

import lombok.RequiredArgsConstructor;
import org.pokeherb.hubservice.application.hub.dto.HubResponse;
import org.pokeherb.hubservice.domain.hub.entity.Hub;
import org.pokeherb.hubservice.domain.hub.exception.HubErrorCode;
import org.pokeherb.hubservice.domain.hub.repository.HubDetailsRepository;
import org.pokeherb.hubservice.domain.hub.repository.HubRepository;
import org.pokeherb.hubservice.global.infrastructure.exception.CustomException;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HubQueryServiceImpl implements HubQueryService {

    private final HubRepository hubRepository;
    private final HubDetailsRepository hubDetailsRepository;

    /**
     * soft delete되지 않은 모든 허브 목록 조회
     * - pagination
     * */
    @Override
    public Page<HubResponse> getHubList(Pageable pageable) {
        Page<Hub> hubs = hubRepository.findAllByDeletedAtIsNull(pageable);
        return hubs.map(HubResponse::from);
    }

    /**
     * hub Id를 통해 단일 허브 정보 조회
     * */
    @Override
    @Cacheable(cacheNames = "hubCache", key = "#hubId")
    public HubResponse getHub(Long hubId) {
        Hub hub = hubRepository.findByHubIdAndDeletedAtIsNull(hubId).orElseThrow(() -> new CustomException(HubErrorCode.HUB_NOT_FOUND));
        return HubResponse.from(hub);
    }

    /**
     * keyword를 포함하는 허브 정보 검색
     * -pagination
     * */
    @Override
    public Page<HubResponse> searchHubList(String keyword, Pageable pageable) {
        Page<Hub> hubs = hubDetailsRepository.searchHubByKeyword(keyword, pageable);
        return hubs.map(HubResponse::from);
    }

    /**
     * hub Id를 통해 해당 허브의 존재 여부를 확인
     * */
    @Override
    public Boolean existsHub(Long id) {
        Hub hub = hubRepository.findByHubIdAndDeletedAtIsNull(id).orElse(null);
        return hub != null;
    }
}
