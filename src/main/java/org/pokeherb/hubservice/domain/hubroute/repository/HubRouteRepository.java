package org.pokeherb.hubservice.domain.hubroute.repository;

import org.pokeherb.hubservice.domain.hubroute.entity.HubRoute;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HubRouteRepository extends JpaRepository<HubRoute, Long> {
    List<HubRoute> findByStartHubIdOrEndHubId(Long startHubId, Long endHubId);
}
