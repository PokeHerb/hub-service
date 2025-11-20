package org.pokeherb.hubservice.infrastructure.persistence;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.pokeherb.hubservice.domain.hub.repository.HubDetailsRepository;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class HubDetailsDao implements HubDetailsRepository {

    private final JPAQueryFactory jpaQueryFactory;
}
