package org.sopt.domain.user.dto.jwt;

import org.sopt.domain.user.domain.RefreshToken;
import org.springframework.security.core.Authentication;

public record ValidatedTokenResult(
        Authentication authentication,
        RefreshToken refreshToken
) {}

