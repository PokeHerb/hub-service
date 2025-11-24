package org.pokeherb.hubservice.infrastructure.persistence;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.pokeherb.hubservice.domain.hub.entity.QHub;
import org.pokeherb.hubservice.domain.hubroute.entity.HubRoute;
import org.pokeherb.hubservice.domain.hubroute.entity.QHubRoute;
import org.pokeherb.hubservice.domain.hubroute.repository.HubRouteDetailsRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class HubRouteDetailsDao implements HubRouteDetailsRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<HubRoute> searchHubRouteByKeyword(String keyword) {
        QHubRoute hubRoute = QHubRoute.hubRoute;
        QHub startHub =  new QHub("startHub");
        QHub endHub = new QHub("endHub");
        return jpaQueryFactory.selectFrom(hubRoute)
                .leftJoin(startHub).on(startHub.hubId.eq(hubRoute.startHubId))
                .leftJoin(endHub).on(endHub.hubId.eq(hubRoute.endHubId))
                .where(
                        containsKeyword(hubRoute, startHub, endHub, keyword)
                )
                .fetch();
    }

    private BooleanBuilder containsKeyword(QHubRoute hubRoute, QHub startHub, QHub endHub, String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return null;
        }

        StringExpression[] startFields = {
                startHub.hubName,
                startHub.address.sido,
                startHub.address.sigungu,
                startHub.address.eupmyeon,
                startHub.address.dong,
                startHub.address.ri,
                startHub.address.street
        };

        StringExpression[] endFields = {
                endHub.hubName,
                endHub.address.sido,
                endHub.address.sigungu,
                endHub.address.eupmyeon,
                endHub.address.dong,
                endHub.address.ri,
                endHub.address.street
        };

        BooleanBuilder keywordBuilder = new BooleanBuilder();
        for (StringExpression f : startFields) {
            keywordBuilder.or(f.containsIgnoreCase(keyword));
        }

        for (StringExpression f : endFields) {
            keywordBuilder.or(f.containsIgnoreCase(keyword));
        }

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(hubRoute.deletedAt.isNull());
        builder.and(keywordBuilder);

        return builder;
    }
}
