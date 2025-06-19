package org.sopt.domain.user.security;

import org.sopt.domain.user.dto.jwt.CustomUser;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class JwtAuthentication extends AbstractAuthenticationToken {

    private final CustomUser principal;
    private final String credentials;

    public JwtAuthentication(CustomUser principal, String credentials, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        super.setAuthenticated(true);

        this.principal = principal;
        this.credentials = credentials;
    }

    @Override
    public CustomUser getPrincipal() {
        return principal;
    }

    @Override
    public String getCredentials() {
        return credentials;
    }

    @Override
    public String getName() {
        return principal.loginId();
    }
}
