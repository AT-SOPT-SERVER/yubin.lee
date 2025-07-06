package org.sopt.domain.user.dto.response;

import lombok.Builder;

@Builder
public record TokenDto(
        String grantType,
        String accessToken,
        String refreshToken,
        Long accessTokenExpiresIn
) {
    public static TokenDto of(String grantType, String accessToken, String refreshToken, Long accessTokenExpiresIn) {
        return TokenDto.builder()
                .grantType(grantType)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .accessTokenExpiresIn(accessTokenExpiresIn)
                .build();
    }
}
