package org.pokeherb.hubservice.domain.hubroute.repository;

import org.pokeherb.hubservice.domain.hubroute.entity.HubRoute;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface HubRouteDetailsRepository {
    Page<HubRoute> searchHubRouteByKeyword(String keyword, Pageable pageable);
}
