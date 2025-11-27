package org.pokeherb.hubservice.domain.hubroute.repository;

import org.pokeherb.hubservice.domain.hubroute.entity.HubRoute;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface HubRouteRepository extends JpaRepository<HubRoute, Long> {
    @Query("SELECT h FROM HubRoute h " +
            "WHERE (h.startHubId = :startHubId OR h.endHubId = :endHubId) " +
            "AND h.deletedAt IS NULL")
    List<HubRoute> findByStartHubIdOrEndHubId(Long startHubId, Long endHubId);

    Optional<HubRoute> findByStartHubIdAndEndHubIdAndDeletedAtIsNull(Long startHubId, Long endHubId);

    Page<HubRoute> findAllByDeletedAtIsNull(Pageable pageable);
}
