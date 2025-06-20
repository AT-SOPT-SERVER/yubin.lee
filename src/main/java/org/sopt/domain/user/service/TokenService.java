package org.sopt.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.sopt.domain.user.dto.jwt.UserDetails;
import org.sopt.domain.user.dto.jwt.ValidatedTokenResult;
import org.sopt.domain.user.dto.response.TokenDto;
import org.sopt.domain.user.domain.BlacklistedToken;
import org.sopt.domain.user.domain.RefreshToken;
import org.sopt.domain.user.domain.User;
import org.sopt.domain.user.repository.BlackListedTokenRepository;
import org.sopt.domain.user.repository.RefreshTokenRepository;
import org.sopt.domain.user.security.JwtAuthentication;
import org.sopt.domain.user.security.JwtProvider;
import org.sopt.global.enums.ErrorCode;
import org.sopt.global.exception.UnauthenticatedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final JwtProvider jwtProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final BlackListedTokenRepository blackListedTokenRepository;

    @Transactional
    public TokenDto generateAndSaveToken(User user) {
        List<GrantedAuthority> authorities = List.of(
                new SimpleGrantedAuthority(user.getRole().name())
        );

        UserDetails principal = UserDetails.from(user);
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

    public ValidatedTokenResult validateRefreshToken(String refreshToken) {
        if (isBlacklisted(refreshToken)) {
            throw new UnauthenticatedException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        if (!jwtProvider.validateToken(refreshToken)){
            throw new UnauthenticatedException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        Authentication authentication = jwtProvider.getAuthentication(refreshToken);

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

    @Transactional
    public void addBlacklistToken(String refreshToken) {
        LocalDateTime expirationAt = jwtProvider.getExpirationTime(refreshToken);
        BlacklistedToken entity = BlacklistedToken.builder()
                .token(refreshToken)
                .expirationAt(expirationAt)
                .build();

        blackListedTokenRepository.save(entity);
    }

    private boolean isBlacklisted(String refreshToken) {
        return blackListedTokenRepository.existsByToken(refreshToken);
    }
}
