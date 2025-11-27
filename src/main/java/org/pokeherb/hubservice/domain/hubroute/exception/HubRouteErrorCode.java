package org.pokeherb.hubservice.domain.hubroute.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.pokeherb.hubservice.global.infrastructure.error.BaseErrorCode;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum HubRouteErrorCode implements BaseErrorCode {
    HUB_ROUTE_NOT_FOUND(HttpStatus.NOT_FOUND, "HUB_ROUTE404", "해당 허브 간 이동 정보가 존재하지 않습니다."),
    INVALID_COST_TYPE(HttpStatus.BAD_REQUEST, "COST_INVALID400", "허브 경로 계산을 위한 cost 타입이 유효하지 않습니다. cost 타입은 duration과 distance만 가능합니다."),
    INVALID_HUB_ID(HttpStatus.BAD_REQUEST, "HUB_ID400", "출발 허브 및 도착 허브 id는 필수로 입력해야 합니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
