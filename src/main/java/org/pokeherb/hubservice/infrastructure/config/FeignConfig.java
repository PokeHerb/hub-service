package org.pokeherb.hubservice.infrastructure.config;

import feign.RequestInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Configuration
@EnableFeignClients("org.pokeherb.hubservice")
@RequiredArgsConstructor
public class FeignConfig {

    private static final String HEADER_USER_ID = "X-User-Id";
    private static final String HEADER_USERNAME = "X-Username";
    private static final String HEADER_ROLES = "X-User-Roles";
    private static final String HEADER_EMAIL = "X-User-Email";
    private static final String HEADER_USER_NAME = "X-User-Name";
    private static final String HEADER_HUB_ID = "X-Hub-Id";
    private static final String HEADER_VENDOR_ID = "X-Vendor-Id";

    /**
     * OpenFeign 요청시 JWT 토큰이 있다면 함께 전송
     * @return
     */
    @Bean
    public RequestInterceptor requestInterceptor() {


        return tpl -> {
            RequestAttributes attrs = RequestContextHolder.getRequestAttributes();
            if (attrs instanceof ServletRequestAttributes) {
                HttpServletRequest req = ((ServletRequestAttributes) attrs).getRequest();
                String authorization = req.getHeader("Authorization");
                if (StringUtils.hasText(authorization)) { // user-service 인증 용
                    tpl.header("Authorization", authorization);
                }

                String userId = req.getHeader(HEADER_USER_ID);
                String username = req.getHeader(HEADER_USERNAME);
                String name = req.getHeader(HEADER_USER_NAME);
                String email = req.getHeader(HEADER_EMAIL);
                String roles = req.getHeader(HEADER_ROLES);
                String hubId = req.getHeader(HEADER_HUB_ID);
                String vendorId = req.getHeader(HEADER_VENDOR_ID);

                if (StringUtils.hasText(userId)) tpl.header(HEADER_USER_ID, userId);
                if (StringUtils.hasText(username)) tpl.header(HEADER_USERNAME, username);
                if (StringUtils.hasText(name)) tpl.header(HEADER_USER_NAME, name);
                if (StringUtils.hasText(email)) tpl.header(HEADER_EMAIL, email);
                if (StringUtils.hasText(roles)) tpl.header(HEADER_ROLES, roles);
                if (StringUtils.hasText(hubId)) tpl.header(HEADER_HUB_ID, hubId);
                if (StringUtils.hasText(vendorId)) tpl.header(HEADER_VENDOR_ID, vendorId);
            }

        };
    }

}
