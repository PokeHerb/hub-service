package org.pokeherb.hubservice.infrastructure.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.pokeherb.hubservice.domain.hub.entity.Hub;
import org.pokeherb.hubservice.domain.hub.repository.HubRepository;
import org.pokeherb.hubservice.domain.hub.service.AddressToCoordinateConverter;
import org.pokeherb.hubservice.domain.hub.service.CheckAccessHub;
import org.pokeherb.hubservice.domain.hubroute.service.TravelInfoCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;


@SpringBootTest
@ActiveProfiles("test")
public class KaKaoApiTest {

    @Autowired
    AddressToCoordinateConverter converter;

    @Autowired
    TravelInfoCalculator calculator;

    @Autowired
    HubRepository hubRepository;

    @Autowired
    CheckAccessHub checkAccessHub;

    @Autowired
    AddressToCoordinateConverter addressConverter;

    private Long startHubId;
    private Long endHubId;

    @BeforeEach
    void setup() {
        Hub startHub = Hub.builder()
                .sido("서울특별시")
                .sigungu("송파구")
                .street("송파대로")
                .building_no("55")
                .checkAccessHub(checkAccessHub)
                .converter(addressConverter)
                .build();

        Hub endHub = Hub.builder()
                .sido("경기도")
                .sigungu("고양시 덕양구")
                .street("권율대로")
                .building_no("570")
                .checkAccessHub(checkAccessHub)
                .converter(addressConverter)
                .build();

        hubRepository.save(startHub);
        hubRepository.save(endHub);

        startHubId = startHub.getHubId();
        endHubId = endHub.getHubId();
    }

    @Test
    @DisplayName(value = "카카오 API 주소 좌표 변환 테스트")
    void kakaoApiGeoCodingTest() {
        List<Double> coordinate = converter.convert("서울특별시 송파구 송파대로 55");
        System.out.println(coordinate);
    }

    @Test
    @DisplayName(value = "카카오 API 경로 계산 테스트")
    void kakaoApiNaviTest() {

        List<Double> travelInfo = calculator.calculateTravelInfo(startHubId, endHubId);
        System.out.println(travelInfo);
    }

}
