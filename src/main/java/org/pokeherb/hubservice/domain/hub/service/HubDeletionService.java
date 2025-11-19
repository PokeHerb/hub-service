package org.pokeherb.hubservice.domain.hub.service;

import org.pokeherb.hubservice.domain.hub.entity.Hub;

public interface HubDeletionService {
    void deleteHub(Hub hub, String userName);
}
