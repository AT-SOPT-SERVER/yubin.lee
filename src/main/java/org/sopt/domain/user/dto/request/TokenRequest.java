package org.sopt.domain.user.dto.request;

public record TokenRequest(
        String accessToken,
        String refreshToken
) {
}
