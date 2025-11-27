package org.pokeherb.hubservice.infrastructure.persistence;

import lombok.RequiredArgsConstructor;
import org.pokeherb.hubservice.domain.hubroute.exception.HubRouteErrorCode;
import org.pokeherb.hubservice.domain.hubroute.service.FinalRouteFactory;
import org.pokeherb.hubservice.global.infrastructure.exception.CustomException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class PgroutingFinalRouteFactory implements FinalRouteFactory {

    private final JdbcTemplate jdbcTemplate;

    private static final Map<String, String> COST_COLUMN_MAP = Map.of(
            "duration", "duration",
            "distance", "distance"
    );

    /**
     * pgRouting 기능을 활용하여 최소 비용 경로 계산 (다익스트라 알고리즘)
     * */
    @Override
    public List<Long> getRouteSequence(Long startHubId, Long endHubId, String cost) {

        String costType = COST_COLUMN_MAP.get(cost);
        if (costType == null) {
            throw new CustomException(HubRouteErrorCode.INVALID_COST_TYPE);
        }

        String sql = String.format("""
                SELECT route.node
                FROM pgr_dijkstra(
                    'SELECT hub_route_id AS id,
                            start_hub_id AS source,
                            end_hub_id AS target,
                            %s AS cost
                    FROM p_hub_route
                    WHERE deleted_at IS NULL',
                    ?, ?,
                    directed := true
                ) AS route
                ORDER BY route.seq;
                """, COST_COLUMN_MAP.get(cost));
        return jdbcTemplate.query(
                sql,
                (rs, rowNum) -> rs.getLong("node"),
                startHubId, endHubId
        );
    }
}
