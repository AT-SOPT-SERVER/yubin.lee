package org.sopt.domain.user.dto.jwt;

import org.sopt.domain.user.model.RefreshToken;
import org.springframework.security.core.Authentication;

public record ValidatedTokenResult(
        Authentication authentication,
        RefreshToken refreshToken
) {}

