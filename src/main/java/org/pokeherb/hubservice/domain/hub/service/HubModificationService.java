package org.pokeherb.hubservice.domain.hub.service;

import org.pokeherb.hubservice.domain.hub.entity.Hub;

public interface HubModificationService {
    // TODO : 허브 주소가 수정되면 허브 간 이동 정보도 수정
    Hub modifyHubAddress(String hubName, String sido, String sigungu, String eupmyeon, String dong, String ri, String street, String building_no, String details);
}
