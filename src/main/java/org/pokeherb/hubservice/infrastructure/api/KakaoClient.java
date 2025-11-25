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

    private RestClient createRestClient(String baseUrl) {
        return RestClient.builder().baseUrl(baseUrl).build();
    }

    /**
     * 카카오 API로 get 요청
     * */
    public JsonNode get(String baseUrl) {
        RestClient client = createRestClient(baseUrl);

        ResponseEntity<String> res = client.get()
                .header("Authorization", "KakaoAK " + kakaoApiKey)
                .header("Content-Type", "application/json")
                .retrieve()
                .toEntity(String.class);

        return handleResponse(res);
    }

    /**
     * 카카오 API로 post 요청
     * */
    public JsonNode post(String baseUrl, Object body) {
        RestClient client = createRestClient(baseUrl);

        ResponseEntity<String> res = client.post()
                .header("Authorization", "KakaoAK " + kakaoApiKey)
                .body(body)
                .retrieve()
                .toEntity(String.class);

        return handleResponse(res);
    }

    private JsonNode handleResponse(ResponseEntity<String> res) {
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
