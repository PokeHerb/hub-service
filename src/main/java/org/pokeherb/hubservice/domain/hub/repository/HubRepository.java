package org.pokeherb.hubservice.domain.hub.repository;

import org.pokeherb.hubservice.domain.hub.entity.Hub;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface HubRepository extends JpaRepository<Hub, Long> {

    Optional<Hub> findByHubIdAndDeletedAtIsNull(Long hubId);

    List<Hub> findByHubIdInAndDeletedAtIsNull(List<Long> hubIds);

    Page<Hub> findAllByDeletedAtIsNull(Pageable pageable);
}
