package org.pokeherb.hubservice.domain.hub.repository;

import org.hibernate.annotations.SQLRestriction;
import org.pokeherb.hubservice.domain.hub.entity.Hub;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HubRepository extends JpaRepository<Hub, Long> {
    @SQLRestriction("deleted_at is null")
    Optional<Hub> findByHubId(Long hubId);

    @SQLRestriction("deleted_at is null")
    Optional<Hub> findByHubName(String hubName);
}
