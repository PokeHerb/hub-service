package org.pokeherb.hubservice.infrastructure.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pokeherb.hubservice.domain.hub.exception.HubErrorCode;
import org.pokeherb.hubservice.domain.hub.repository.HubRepository;
import org.pokeherb.hubservice.domain.hub.value.Coordinate;
import org.pokeherb.hubservice.domain.hubroute.service.TravelInfoCalculator;
import org.pokeherb.hubservice.global.infrastructure.exception.CustomException;
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
public class KakaoTravelInfoCalculator implements TravelInfoCalculator {

    @Value("${api.kakao.secret}")
    private String kakaoApiKey;

    private final ObjectMapper om;

    private final HubRepository hubRepository;

    @Override
    public List<Double> calculateTravelInfo(Long startHubId, Long endHubId) {
        Coordinate startCoordinate = hubRepository.findByHubIdAndDeletedAtIsNull(startHubId).orElseThrow(() -> new CustomException(HubErrorCode.HUB_NOT_FOUND)).getCoordinate();
        Coordinate endCoordinate = hubRepository.findByHubIdAndDeletedAtIsNull(endHubId).orElseThrow(() -> new CustomException(HubErrorCode.HUB_NOT_FOUND)).getCoordinate();

        RestClient client = RestClient
                .builder()
                .baseUrl("https://apis-navi.kakaomobility.com/v1/directions?origin=" + startCoordinate.getLongitude() +"," + startCoordinate.getLatitude() + "&destination=" + endCoordinate.getLongitude() + "," + endCoordinate.getLatitude())
                .build();
        ResponseEntity<String> res = client.get()
                .header("Authorization", "KakaoAK " + kakaoApiKey)
                .retrieve()
                .toEntity(String.class);

        if (res.getStatusCode().is2xxSuccessful()) {
            try {
                JsonNode node = om.readTree(res.getBody());
                JsonNode docs = node.get("routes").get(0).get("summary");
                log.info("docs {}", docs);
                if (!docs.isEmpty()) {
                    double duration = docs.get("duration").asDouble();
                    double distance = docs.get("distance").asDouble();
                    return List.of(duration, distance);
                }
            } catch (JsonProcessingException e) {
                log.error("카카오 경로 계산 과정 오류", e);
            }
        }
        return List.of();
    }
}
