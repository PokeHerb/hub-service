package org.pokeherb.hubservice.domain.hubroute.repository;

import org.pokeherb.hubservice.domain.hubroute.entity.HubRoute;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface HubRouteRepository extends JpaRepository<HubRoute, Long> {
    List<HubRoute> findByStartHubIdOrEndHubId(Long startHubId, Long endHubId);
    Optional<HubRoute> findByStartHubIdAndEndHubId(Long startHubId, Long endHubId);
}
