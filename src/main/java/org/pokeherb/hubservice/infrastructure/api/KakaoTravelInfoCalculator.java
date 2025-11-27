package org.pokeherb.hubservice.infrastructure.api;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pokeherb.hubservice.application.hub.dto.HubResponse;
import org.pokeherb.hubservice.domain.hub.exception.HubErrorCode;
import org.pokeherb.hubservice.domain.hub.repository.HubRepository;
import org.pokeherb.hubservice.domain.hub.value.Coordinate;
import org.pokeherb.hubservice.domain.hubroute.service.TravelInfoCalculator;
import org.pokeherb.hubservice.global.infrastructure.exception.CustomException;
import org.pokeherb.hubservice.infrastructure.exception.ApiErrorCode;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    @Override
    public Map<String, Double> calculateFinalTravelInfo(List<HubResponse> routeSequence, Map<String, Double> destination) {
        log.info("계산 시작");
        if (routeSequence == null || routeSequence.isEmpty()) {
            log.error("routeSequence is empty");
            throw new CustomException(ApiErrorCode.INVALID_ARGUMENT_FOR_ROUTE);
        }
        Map<String, Object> body = new HashMap<>();
        List<Map<String, String>> waypoints = new ArrayList<>();
        Map<String, String> origin = new HashMap<>();
        for (HubResponse hubResponse : routeSequence) {
            if (origin.isEmpty()) {
                log.error("origin is empty");
                origin.put("x", hubResponse.longitude().toString());
                origin.put("y", hubResponse.latitude().toString());
            }
            Map<String, String> route = new HashMap<>();
            route.put("x", hubResponse.longitude().toString());
            route.put("y", hubResponse.latitude().toString());
            waypoints.add(route);
        }
        log.info("check");
        Map<String, String> destinations = Map.of("x", destination.get("longitude").toString(), "y", destination.get("latitude").toString());
        log.info("destinations {}", destinations);
        body.put("origin", origin);
        body.put("destination", destinations);
        body.put("waypoints", waypoints);
        String baseUrl = "https://apis-navi.kakaomobility.com/v1/waypoints/directions";
        JsonNode node = kakaoClient.post(baseUrl, body);
        JsonNode docs = node.get("routes").get(0).get("summary");
        log.info("docs {}", docs);
        if (!docs.isEmpty()) {
            double finalDuration = docs.get("duration").asDouble();
            double finalDistance = docs.get("distance").asDouble();
            log.info("final duration {}", finalDuration);
            return Map.of("finalDuration", finalDuration, "finalDistance", finalDistance);
        }
        return Map.of();
    }
}
