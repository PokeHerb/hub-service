package org.pokeherb.hubservice.application.hub.command;

import org.pokeherb.hubservice.application.hub.dto.HubCreationRequest;
import org.pokeherb.hubservice.application.hub.dto.HubModificationRequest;
import org.pokeherb.hubservice.application.hub.dto.HubResponse;

public interface HubCommandService {
    // 허브 생성
    HubResponse createHub(HubCreationRequest request);
    // 허브 수정
    HubResponse modifyHub(Long hubId, HubModificationRequest request);
    // 허브 삭제
    void deleteHub(Long hubId);
}
