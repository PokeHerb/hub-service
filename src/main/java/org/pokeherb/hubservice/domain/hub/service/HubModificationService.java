package org.pokeherb.hubservice.domain.hub.service;

import org.pokeherb.hubservice.domain.hub.entity.Hub;

public interface HubModificationService {
    Hub modifyHubAddress(Hub hub, String sido, String sigungu, String eupmyeon, String dong, String ri, String street, String building_no, String details);
}
