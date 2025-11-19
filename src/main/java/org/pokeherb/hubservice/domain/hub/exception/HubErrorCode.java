package org.pokeherb.hubservice.domain.hub.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.pokeherb.hubservice.global.infrastructure.error.BaseErrorCode;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum HubErrorCode implements BaseErrorCode {
    HUB_NOT_FOUND(HttpStatus.NOT_FOUND, "HUB404", "해당 허브가 존재하지 않습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
