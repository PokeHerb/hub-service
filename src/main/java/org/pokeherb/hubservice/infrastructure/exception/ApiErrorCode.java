package org.pokeherb.hubservice.infrastructure.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.pokeherb.hubservice.global.infrastructure.error.BaseErrorCode;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ApiErrorCode implements BaseErrorCode {
    KAKAO_API_CALL_FAILED(HttpStatus.BAD_GATEWAY, "KAKAO_API_502", "카카오 API 호출에 실패했습니다."),
    KAKAO_API_RESPONSE_PARSE_FAILED(HttpStatus.BAD_GATEWAY, "KAKAO_PARSE_502", "카카오 API 응답을 파싱하는 데 실패했습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
