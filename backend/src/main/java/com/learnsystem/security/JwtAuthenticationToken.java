package com.learnsystem.security;

import java.util.Collection;
import java.util.List;
import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

@Getter
public class JwtAuthenticationToken extends AbstractAuthenticationToken {
    private final Long userId;
    private final String username;
    private final String role;

    public JwtAuthenticationToken(Long userId, String username, String role, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.userId = userId;
        this.username = username;
        this.role = role;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return "";
    }

    @Override
    public Object getPrincipal() {
        return username;
    }

    public static JwtAuthenticationToken of(Long userId, String username, String role) {
        return new JwtAuthenticationToken(userId, username, role, List.of(() -> "ROLE_" + role));
    }
}
