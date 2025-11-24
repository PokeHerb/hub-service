package org.pokeherb.hubservice.infrastructure.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pokeherb.hubservice.domain.hub.service.AddressToCoordinateConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Slf4j
@Service
@RefreshScope
@RequiredArgsConstructor
public class KakaoAddressToCoordinateConverter implements AddressToCoordinateConverter {

    @Value("${api.kakao.secret}")
    private String kakaoApiKey;

    private final ObjectMapper om;

    @Override
    public List<Double> convert(String address) {
        RestClient client = RestClient
                .builder()
                .baseUrl("https://dapi.kakao.com/v2/local/search/address.json?query=" + address)
                .build();
        ResponseEntity<String> res = client.get()
                .header("Authorization", "KakaoAK " + kakaoApiKey)
                .retrieve()
                .toEntity(String.class);

        if (res.getStatusCode().is2xxSuccessful()) {
            try {
                JsonNode node = om.readTree(res.getBody());
                JsonNode docs = node.get("documents").get(0);
                log.info("docs {}", docs);
                if (!docs.isEmpty()) {
                    double latitude = docs.get("y").asDouble();
                    double longitude = docs.get("x").asDouble();
                    return List.of(latitude, longitude);
                }
            } catch (JsonProcessingException e) {
                log.error("카카오 주소 좌표 변환 과정 요류", e);
            }
        }
        return List.of();
    }
}
