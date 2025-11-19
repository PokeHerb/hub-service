package org.pokeherb.hubservice.domain.hubroute.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.pokeherb.hubservice.global.infrastructure.error.BaseErrorCode;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum HubRouteErrorCode implements BaseErrorCode {
    HUB_ROUTE_NOT_FOUND(HttpStatus.NOT_FOUND, "HUB_ROUTE404", "해당 허브 간 이동 정보가 존재하지 않습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
