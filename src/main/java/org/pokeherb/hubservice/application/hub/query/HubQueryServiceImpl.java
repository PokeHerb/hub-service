package org.pokeherb.hubservice.application.hub.query;

import lombok.RequiredArgsConstructor;
import org.pokeherb.hubservice.application.hub.dto.HubResponse;
import org.pokeherb.hubservice.domain.hub.entity.Hub;
import org.pokeherb.hubservice.domain.hub.exception.HubErrorCode;
import org.pokeherb.hubservice.domain.hub.repository.HubRepository;
import org.pokeherb.hubservice.global.infrastructure.exception.CustomException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HubQueryServiceImpl implements HubQueryService {

    private final HubRepository hubRepository;

    @Override
    public List<HubResponse> getHubList() {
        List<Hub> hubs = hubRepository.findAllByDeletedAtIsNull();
        return hubs.stream().map(HubResponse::from).toList();
    }

    @Override
    public HubResponse getHub(Long hubId) {
        Hub hub = hubRepository.findByHubId(hubId).orElseThrow(() -> new CustomException(HubErrorCode.HUB_NOT_FOUND));
        return HubResponse.from(hub);
    }
}
