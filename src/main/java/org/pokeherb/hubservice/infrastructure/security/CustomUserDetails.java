package org.pokeherb.hubservice.infrastructure.security;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Getter
@ToString
public class CustomUserDetails implements UserDetails {

    private final UUID userId;
    private final String username;
    private final String name;
    private final String email;
    private final String roles;
    private final Long hubId;
    private final UUID vendorId;

    @Builder
    public CustomUserDetails(UUID userId, String username, String name, String email, String roles, Long hubId, UUID vendorId) {
        this.userId = userId;
        this.username = username;
        this.name = name;
        this.email = email;
        this.roles = roles;
        this.hubId = hubId;
        this.vendorId = vendorId;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<String> userRoles = StringUtils.hasText(roles) ? Arrays.stream(roles.split(",")).toList() : List.of("ROLE_PENDING");
        return userRoles.stream().map(SimpleGrantedAuthority::new).toList();
    }

    @Override
    public String getPassword() {
        return "";
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isEnabled() {
        // 회원 승인 여부
        // ROLE_PENDING이 포함되어 있다면 미승인, 없으면 승인
        return !roles.contains("ROLE_PENDING");
    }
}
