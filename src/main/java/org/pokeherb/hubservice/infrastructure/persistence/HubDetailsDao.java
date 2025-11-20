package org.pokeherb.hubservice.infrastructure.persistence;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.pokeherb.hubservice.domain.hub.entity.Hub;
import org.pokeherb.hubservice.domain.hub.entity.QHub;
import org.pokeherb.hubservice.domain.hub.repository.HubDetailsRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class HubDetailsDao implements HubDetailsRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Hub> searchHubByKeyword(String keyword) {
        QHub hub = QHub.hub;
        return jpaQueryFactory.selectFrom(hub)
                .where(hub.hubName.containsIgnoreCase(keyword)
                        .or(hub.address.sido.containsIgnoreCase(keyword))
                        .or(hub.address.sigungu.containsIgnoreCase(keyword))
                        .or(hub.address.eupmyeon.containsIgnoreCase(keyword))
                        .or(hub.address.ri.containsIgnoreCase(keyword))
                        .or(hub.address.dong.containsIgnoreCase(keyword))
                        .or(hub.address.street.containsIgnoreCase(keyword)))
                .fetch();
    }
}
