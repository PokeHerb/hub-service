package org.pokeherb.hubservice.infrastructure.api;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pokeherb.hubservice.domain.hub.exception.HubErrorCode;
import org.pokeherb.hubservice.domain.hub.service.AddressToCoordinateConverter;
import org.pokeherb.hubservice.global.infrastructure.exception.CustomException;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoAddressToCoordinateConverter implements AddressToCoordinateConverter {

    private final KakaoClient kakaoClient;

    /**
     * 문자열 주소를 좌표로 변환
     * */
    @Override
    public Map<String, Double> convert(String address) {
        String baseUrl = "https://dapi.kakao.com/v2/local/search/address.json?query=" + address;
        JsonNode node = kakaoClient.get(baseUrl);
        JsonNode documents = node.get("documents");

        // documents가 비어있을 경우 잘못된 주소로 인한 변환 오류
        if  (documents == null || documents.isEmpty()) {
            throw new CustomException(HubErrorCode.INVALID_ADDRESS);
        }
        JsonNode docs = node.get("documents").get(0);
        log.info("docs {}", docs);

        // docs에 x, y가 없을 경우 잘못된 주소로 인한 변환 오류
        if (!docs.hasNonNull("x") || !docs.hasNonNull("y")) {
            throw new CustomException(HubErrorCode.INVALID_ADDRESS);
        }
        double latitude = docs.get("y").asDouble();
        double longitude = docs.get("x").asDouble();

        // 반환된 좌표값의 이상치 확인
        if (latitude < -90 || latitude > 90 || longitude < -180 || longitude > 180) {
            throw new CustomException(HubErrorCode.INVALID_ADDRESS);
        }
        return Map.of("latitude", latitude, "longitude", longitude);
    }
}
