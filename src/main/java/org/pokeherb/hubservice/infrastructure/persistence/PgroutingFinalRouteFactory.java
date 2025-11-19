package org.pokeherb.hubservice.infrastructure.persistence;

import lombok.RequiredArgsConstructor;
import org.pokeherb.hubservice.domain.hubroute.service.FinalRouteFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class PgroutingFinalRouteFactory implements FinalRouteFactory {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Long> getRouteSequence(Long startHubId, Long endHubId) {
        String sql = """
                SELECT route.node
                FROM pgr_dijkstra(
                    'SELECT hub_route_id AS id,
                            start_hub_id AS source,
                            end_hub_id AS target,
                            duration AS cost
                    FROM p_hub_route
                    WHERE deleted_at IS NULL',
                    ?, ?,
                    directed := true
                ) AS route
                ORDER BY route.seq;
                """;
        return jdbcTemplate.query(
                sql,
                (rs, rowNum) -> rs.getLong("node"),
                startHubId, endHubId
        );
    }
}
