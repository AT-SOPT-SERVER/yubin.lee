package org.sopt.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.sopt.domain.user.dto.jwt.CustomUser;
import org.sopt.domain.user.dto.jwt.ValidatedTokenResult;
import org.sopt.domain.user.dto.response.TokenDto;
import org.sopt.domain.user.model.RefreshToken;
import org.sopt.domain.user.model.User;
import org.sopt.domain.user.repository.RefreshTokenRepository;
import org.sopt.domain.user.security.JwtAuthentication;
import org.sopt.domain.user.security.JwtProvider;
import org.sopt.global.ErrorCode;
import org.sopt.global.exception.UnauthenticatedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
class TokenService {

    private final JwtProvider jwtProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public TokenDto generateAndSaveToken(User user) {
        List<GrantedAuthority> authorities = List.of(
                new SimpleGrantedAuthority(user.getRole().name())
        );

        CustomUser principal = CustomUser.from(user);
        Authentication authentication = new JwtAuthentication(principal, null, authorities);

        TokenDto tokenDto = jwtProvider.generateToken(authentication);

        saveRefreshToken(authentication.getName(), tokenDto.refreshToken());
        return tokenDto;
    }

    private void saveRefreshToken(String loginId, String refreshToken) {
        RefreshToken token = RefreshToken.builder()
                .key(loginId)
                .value(refreshToken)
                .build();

        refreshTokenRepository.save(token);
    }

    public ValidatedTokenResult validateRefreshToken(String accessToken, String refreshToken) {
        if (!jwtProvider.validateToken(refreshToken)){
            throw new UnauthenticatedException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        Authentication authentication = jwtProvider.getAuthentication(accessToken);

        RefreshToken savedToken = refreshTokenRepository.findByKey(authentication.getName())
                .orElseThrow(() -> new UnauthenticatedException(ErrorCode.UN_AUTHENTICATION));

        return new ValidatedTokenResult(authentication, savedToken);
    }

    @Transactional
    public TokenDto generateNewRefreshToken(Authentication authentication, RefreshToken refreshToken) {
        if (jwtProvider.refreshTokenPeriodCheck(refreshToken.getValue())) {
            // 3일 미만 → access + refresh 모두 재발급
            TokenDto newToken = jwtProvider.generateToken(authentication);
            refreshToken.updateValue(newToken.refreshToken());
            return newToken;
        }

        // 3일 이상 → access만 재발급
        return jwtProvider.refreshAccessToken(authentication, refreshToken);
    }
}
