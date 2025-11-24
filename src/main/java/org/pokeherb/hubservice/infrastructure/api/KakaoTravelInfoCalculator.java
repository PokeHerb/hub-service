package org.pokeherb.hubservice.infrastructure.api;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pokeherb.hubservice.domain.hub.exception.HubErrorCode;
import org.pokeherb.hubservice.domain.hub.repository.HubRepository;
import org.pokeherb.hubservice.domain.hub.value.Coordinate;
import org.pokeherb.hubservice.domain.hubroute.service.TravelInfoCalculator;
import org.pokeherb.hubservice.global.infrastructure.exception.CustomException;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
@RefreshScope
@RequiredArgsConstructor
public class KakaoTravelInfoCalculator implements TravelInfoCalculator {

    private final KakaoClient kakaoClient;
    private final HubRepository hubRepository;

    @Override
    public Map<String, Double> calculateTravelInfo(Long startHubId, Long endHubId) {
        Coordinate startCoordinate = hubRepository.findByHubIdAndDeletedAtIsNull(startHubId).orElseThrow(() -> new CustomException(HubErrorCode.HUB_NOT_FOUND)).getCoordinate();
        Coordinate endCoordinate = hubRepository.findByHubIdAndDeletedAtIsNull(endHubId).orElseThrow(() -> new CustomException(HubErrorCode.HUB_NOT_FOUND)).getCoordinate();

        String baseUrl = "https://apis-navi.kakaomobility.com/v1/directions?origin=" + startCoordinate.getLongitude() +"," + startCoordinate.getLatitude() + "&destination=" + endCoordinate.getLongitude() + "," + endCoordinate.getLatitude();

        JsonNode node = kakaoClient.get(baseUrl);
        JsonNode docs = node.get("routes").get(0).get("summary");
        log.info("docs {}", docs);
        if (!docs.isEmpty()) {
            double duration = docs.get("duration").asDouble();
            double distance = docs.get("distance").asDouble();
            return Map.of("duration", duration, "distance", distance);
        }
        return Map.of();
    }
}
