package org.pokeherb.hubservice.infrastructure.security;

import org.pokeherb.hubservice.domain.hub.service.CheckAccessHub;
import org.pokeherb.hubservice.global.infrastructure.error.GeneralErrorCode;
import org.pokeherb.hubservice.global.infrastructure.exception.CustomException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class SecurityCheckAccessHub implements CheckAccessHub {
    @Override
    public void checkAccess() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isPermitted = false;
        if (auth != null && auth.getPrincipal() instanceof UserDetails userDetails) {
            isPermitted = userDetails.getAuthorities().stream().anyMatch(authority -> authority.getAuthority().equals("ROLE_MASTER"));
        }
        if (!isPermitted) {
            throw new CustomException(GeneralErrorCode.FORBIDDEN_403);
        }
    }
}
