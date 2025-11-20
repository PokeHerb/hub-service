package org.pokeherb.hubservice.domain.hubroute.repository;

import org.pokeherb.hubservice.domain.hubroute.entity.HubRoute;

import java.util.List;

public interface HubRouteDetailsRepository {
    List<HubRoute> searchHubRouteByKeyword(String keyword);
}
