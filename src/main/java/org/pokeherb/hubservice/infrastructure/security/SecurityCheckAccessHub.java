package org.pokeherb.hubservice.infrastructure.security;

import lombok.RequiredArgsConstructor;
import org.pokeherb.hubservice.domain.hub.service.CheckAccessHub;
import org.pokeherb.hubservice.global.infrastructure.error.GeneralErrorCode;
import org.pokeherb.hubservice.global.infrastructure.exception.CustomException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SecurityCheckAccessHub implements CheckAccessHub {

    private final SecurityUtils securityUtils;

    @Override
    public void checkAccess() {
        if (!securityUtils.isPermitted("MASTER")) {
            throw new CustomException(GeneralErrorCode.FORBIDDEN_403);
        }
    }
}
