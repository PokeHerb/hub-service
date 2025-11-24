package org.pokeherb.hubservice.infrastructure.persistence;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.pokeherb.hubservice.domain.hub.entity.Hub;
import org.pokeherb.hubservice.domain.hub.entity.QHub;
import org.pokeherb.hubservice.domain.hub.repository.HubDetailsRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class HubDetailsDao implements HubDetailsRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<Hub> searchHubByKeyword(String keyword, Pageable pageable) {
        QHub hub = QHub.hub;

        // content 쿼리
        List<Hub> content = jpaQueryFactory.selectFrom(hub)
                .where(hub.hubName.containsIgnoreCase(keyword)
                        .or(hub.address.sido.containsIgnoreCase(keyword))
                        .or(hub.address.sigungu.containsIgnoreCase(keyword))
                        .or(hub.address.eupmyeon.containsIgnoreCase(keyword))
                        .or(hub.address.ri.containsIgnoreCase(keyword))
                        .or(hub.address.dong.containsIgnoreCase(keyword))
                        .or(hub.address.street.containsIgnoreCase(keyword)))
                .orderBy(getOrderSpecifiers(pageable))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // count 쿼리
        Long totalCount = jpaQueryFactory.select(hub.count()).from(hub)
                .where(hub.hubName.containsIgnoreCase(keyword)
                        .or(hub.address.sido.containsIgnoreCase(keyword))
                        .or(hub.address.sigungu.containsIgnoreCase(keyword))
                        .or(hub.address.eupmyeon.containsIgnoreCase(keyword))
                        .or(hub.address.ri.containsIgnoreCase(keyword))
                        .or(hub.address.dong.containsIgnoreCase(keyword))
                        .or(hub.address.street.containsIgnoreCase(keyword)))
                .fetchOne();

        // totalCount가 null일 수 있으므로 객체로 받아 정수로 변환
        long total = totalCount != null ? totalCount : 0L;
        return new PageImpl<>(content, pageable, total);
    }

    private OrderSpecifier<?>[] getOrderSpecifiers(Pageable pageable) {
        PathBuilder<Hub> pathBuilder = new PathBuilder<>(Hub.class, "hub");

        return pageable.getSort().stream()
                .map(order -> new OrderSpecifier(
                        order.isAscending() ? Order.ASC : Order.DESC,
                        pathBuilder.get(order.getProperty())
                ))
                .toArray(OrderSpecifier[]::new);
    }
}
