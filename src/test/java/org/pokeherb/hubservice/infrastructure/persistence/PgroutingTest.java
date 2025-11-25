package org.pokeherb.hubservice.infrastructure.persistence;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.pokeherb.hubservice.application.hub.dto.HubResponse;
import org.pokeherb.hubservice.application.hubroute.query.FinalHubRouteQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class PgroutingTest {

    @Autowired
    private FinalHubRouteQueryService finalHubRouteQueryService;

    @Test
    @DisplayName(value = "pgRouting 최종 경로 탐색 테스트")
    void pgRoutingTest() {
        Long startHubId = 1L; // 실제 DB에 존재하는 시작 hub ID
        Long endHubId = 4L;   // 실제 DB에 존재하는 도착 hub ID

        List<HubResponse> hubResponses = finalHubRouteQueryService.getFinalHubRoute(startHubId, endHubId, "duration");

        System.out.println("Route sequence: " + hubResponses);
    }
}
