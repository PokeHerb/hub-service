package org.pokeherb.hubservice.infrastructure.security;

import org.pokeherb.hubservice.domain.hub.service.CheckAccessHub;
import org.springframework.stereotype.Service;

@Service
public class SecurityCheckAccessHub implements CheckAccessHub {
    @Override
    public void checkAccess() {
        // TODO: 현재 로그인 유저의 권한 (ROLE)이 마스터 관리자인지 확인
    }
}
