package org.sopt.domain.user.dto.jwt;

import org.springframework.security.core.Authentication;

public record ValidatedTokenResult(
        Authentication authentication,
        String refreshToken
) {}

