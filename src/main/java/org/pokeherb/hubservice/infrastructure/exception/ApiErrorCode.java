package org.pokeherb.hubservice.infrastructure.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.pokeherb.hubservice.global.infrastructure.error.BaseErrorCode;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ApiErrorCode implements BaseErrorCode {
    KAKAO_API_CALL_FAILED(HttpStatus.BAD_GATEWAY, "KAKAO_API_502", "카카오 API 호출에 실패했습니다."),
    KAKAO_API_RESPONSE_PARSE_FAILED(HttpStatus.BAD_GATEWAY, "KAKAO_PARSE_502", "카카오 API 응답을 파싱하는 데 실패했습니다."),
    INVALID_ARGUMENT_FOR_ROUTE(HttpStatus.BAD_REQUEST, "KAKAO_ROUTE_400", "경로 계산을 위해 출발지와 도착지가 필요합니다."),
    INVALID_ARGUMENT_FOR_COORDINATE(HttpStatus.BAD_REQUEST, "KAKAO_COORDINATE_400", "위도, 경도 좌표가 포함되어 있어야 합니다."),
    FAIL_ASSIGN_HUB_DRIVER(HttpStatus.NOT_FOUND, "HUB_DRIVER_404", "현재 배정할 수 있는 허브 배송 담당자가 없습니다."),
    FAIL_ASSIGN_VENDOR_DRIVER(HttpStatus.NOT_FOUND, "VENDOR_DRIVER_404", "현재 배정할 수 있는 업체 배송 담당자가 없습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
