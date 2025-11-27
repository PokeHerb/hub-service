package org.pokeherb.hubservice.domain.hub.repository;

import org.pokeherb.hubservice.domain.hub.entity.Hub;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface HubDetailsRepository {
    Page<Hub> searchHubByKeyword(String keyword, Pageable pageable);
}
