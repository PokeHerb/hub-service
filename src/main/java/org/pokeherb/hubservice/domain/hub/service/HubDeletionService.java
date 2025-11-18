package org.pokeherb.hubservice.domain.hub.service;

public interface HubDeletionService {
    // TODO : 허브 삭제 시 해당 허브와 관련된 허브 간 이동 정보도 모두 비활성화
    void deleteHub(String userName);
}
