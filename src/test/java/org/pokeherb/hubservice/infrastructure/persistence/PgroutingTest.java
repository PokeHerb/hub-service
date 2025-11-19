package org.pokeherb.hubservice.infrastructure.persistence;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.pokeherb.hubservice.domain.hubroute.service.FinalRouteFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class PgroutingTest {

    @Autowired
    private FinalRouteFactory finalRouteFactory;

    @Test
    @DisplayName(value = "pgRouting 최종 경로 탐색 테스트")
    void pgRoutingTest() {
        Long startHubId = 1L; // 실제 DB에 존재하는 시작 hub ID
        Long endHubId = 4L;   // 실제 DB에 존재하는 도착 hub ID

        List<Long> route = finalRouteFactory.getRouteSequence(startHubId, endHubId);

        System.out.println("Route sequence: " + route);
    }
}
