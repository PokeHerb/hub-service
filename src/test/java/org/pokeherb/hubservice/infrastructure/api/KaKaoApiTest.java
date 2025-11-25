package org.pokeherb.hubservice.infrastructure.api;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.pokeherb.hubservice.application.finalroute.service.FinalRouteService;
import org.pokeherb.hubservice.application.hub.dto.HubResponse;
import org.pokeherb.hubservice.domain.hub.service.AddressToCoordinateConverter;
import org.pokeherb.hubservice.domain.hubroute.service.TravelInfoCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Map;


@SpringBootTest
@ActiveProfiles("test")
public class KaKaoApiTest {

    @Autowired
    AddressToCoordinateConverter converter;

    @Autowired
    TravelInfoCalculator calculator;

    @Autowired
    FinalRouteService finalRouteService;

    @Test
    @DisplayName(value = "카카오 API 주소 좌표 변환 테스트")
    void kakaoApiGeoCodingTest() {
        Map<String, Double> coordinate = converter.convert("서울특별시 송파구 송파대로 55");
        System.out.println(coordinate);
    }

    @Test
    @DisplayName(value = "카카오 API 경로 계산 테스트")
    void kakaoApiNaviTest() {
        Map<String, Double> travelInfo = calculator.calculateTravelInfo(1L, 3L);
        System.out.println(travelInfo);
    }

    @Test
    @DisplayName(value = "카카오 API 다중 경유지 최종 경로 계산 테스트")
    void kakaoApiTravelInfoTest() {
        List<HubResponse> routeSequence = finalRouteService.getFinalHubRoute(1L, 4L, "duration");
        Map<String, String> destination = Map.of(
                "latitude", "35.158695", "longitude", "129.160384"
        );

        Map<String, Double> result = calculator.calculateFinalTravelInfo(routeSequence, destination);
        System.out.println(result);
    }
}
