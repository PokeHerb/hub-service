package org.pokeherb.hubservice.infrastructure.api;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.pokeherb.hubservice.domain.hub.service.AddressToCoordinateConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
public class KaKaoApiTest {

    @Autowired
    AddressToCoordinateConverter converter;

    @Test
    @DisplayName(value = "카카오 API 주소 좌표 변환 테스트")
    void kakaoApiTest() {
        List<Double> coordinate = converter.convert("서울특별시 송파구 송파대로 55");
        System.out.println(coordinate);
    }
}
