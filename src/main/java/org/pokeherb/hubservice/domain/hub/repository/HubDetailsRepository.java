package org.pokeherb.hubservice.domain.hub.repository;

import org.pokeherb.hubservice.domain.hub.entity.Hub;

import java.util.List;

public interface HubDetailsRepository {
    List<Hub> searchHubByKeyword(String keyword);
}
