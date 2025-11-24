package org.pokeherb.hubservice.infrastructure.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.pokeherb.hubservice.global.infrastructure.exception.CustomException;
import org.pokeherb.hubservice.infrastructure.exception.ApiErrorCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@RefreshScope
@RequiredArgsConstructor
public class KakaoClient {

    @Value("${api.kakao.secret}")
    private String kakaoApiKey;

    private final ObjectMapper om;

    public JsonNode get(String baseUrl) {
        RestClient client = RestClient
                .builder()
                .baseUrl(baseUrl)
                .build();

        ResponseEntity<String> res = client.get()
                .header("Authorization", "KakaoAK " + kakaoApiKey)
                .retrieve()
                .toEntity(String.class);
        if (!res.getStatusCode().is2xxSuccessful()) {
            throw new CustomException(ApiErrorCode.KAKAO_API_CALL_FAILED);
        }

        try {
            return om.readTree(res.getBody());
        } catch (JsonProcessingException e) {
            throw new CustomException(ApiErrorCode.KAKAO_API_RESPONSE_PARSE_FAILED);
        }
    }
}
