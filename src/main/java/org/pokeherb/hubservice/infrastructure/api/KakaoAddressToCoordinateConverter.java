package org.pokeherb.hubservice.infrastructure.api;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pokeherb.hubservice.domain.hub.service.AddressToCoordinateConverter;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoAddressToCoordinateConverter implements AddressToCoordinateConverter {

    private final KakaoClient kakaoClient;

    @Override
    public Map<String, Double> convert(String address) {
        String baseUrl = "https://dapi.kakao.com/v2/local/search/address.json?query=" + address;
        JsonNode node = kakaoClient.get(baseUrl);
        JsonNode docs = node.get("documents").get(0);
        log.info("docs {}", docs);
        if (!docs.isEmpty()) {
            double latitude = docs.get("y").asDouble();
            double longitude = docs.get("x").asDouble();
            return Map.of("latitude", latitude, "longitude", longitude);
        }
        return Map.of();
    }
}
