package org.pokeherb.hubservice.domain.hub.repository;

import org.hibernate.annotations.SQLRestriction;
import org.pokeherb.hubservice.domain.hub.entity.Hub;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface HubRepository extends JpaRepository<Hub, UUID> {
    @SQLRestriction("deleted_at is null")
    Optional<Hub> findByHubId(UUID hubId);
}
