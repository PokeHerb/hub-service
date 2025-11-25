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

    @Override
    public Page<HubResponse> getHubList(Pageable pageable) {
        Page<Hub> hubs = hubRepository.findAllByDeletedAtIsNull(pageable);
        return hubs.map(HubResponse::from);
    }

    @Override
    @Cacheable(cacheNames = "hubCache", key = "#hubId")
    public HubResponse getHub(Long hubId) {
        Hub hub = hubRepository.findByHubIdAndDeletedAtIsNull(hubId).orElseThrow(() -> new CustomException(HubErrorCode.HUB_NOT_FOUND));
        return HubResponse.from(hub);
    }

    @Override
    public Page<HubResponse> searchHubList(String keyword, Pageable pageable) {
        Page<Hub> hubs = hubDetailsRepository.searchHubByKeyword(keyword, pageable);
        return hubs.map(HubResponse::from);
    }
}
