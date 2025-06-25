package org.sopt.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.sopt.domain.user.dto.jwt.UserDetails;
import org.sopt.domain.user.dto.jwt.ValidatedTokenResult;
import org.sopt.domain.user.dto.response.TokenDto;
import org.sopt.domain.user.domain.User;
import org.sopt.domain.user.security.JwtAuthentication;
import org.sopt.domain.user.security.JwtProvider;
import org.sopt.global.enums.ErrorCode;
import org.sopt.global.exception.UnauthenticatedException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final JwtProvider jwtProvider;
    private final RedisTemplate<String, String> redisTemplate;
    private static final long THREE_DAYS = 1000L * 60 * 60 * 24 * 3;
    private static final String REFRESH_TOKEN = "refreshToken:";
    private static final String BLACKLISTED_TOKEN = "blacklistedToken:";

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

    public void saveRefreshToken(String loginId, String token) {
        long ttl = jwtProvider.refreshTokenPeriodCheck(token);

        redisTemplate.opsForValue().set( REFRESH_TOKEN + loginId, token, Duration.ofSeconds(ttl));
    }

    public ValidatedTokenResult validateRefreshToken(String refreshToken) {
        if (!jwtProvider.validateToken(refreshToken)){
            throw new UnauthenticatedException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        Authentication authentication = jwtProvider.getAuthentication(refreshToken);

        String savedToken = redisTemplate.opsForValue().get(REFRESH_TOKEN + authentication.getName());

        if (savedToken == null) {
            throw new UnauthenticatedException(ErrorCode.UN_AUTHENTICATION);
        }

        return new ValidatedTokenResult(authentication, savedToken);
    }

    @Transactional
    public TokenDto generateNewRefreshToken(Authentication authentication, String refreshToken) {
        if (jwtProvider.refreshTokenPeriodCheck(refreshToken) < THREE_DAYS) {
            // 3일 미만 → access + refresh 모두 재발급
            TokenDto newToken = jwtProvider.generateToken(authentication);
            long ttl = jwtProvider.refreshTokenPeriodCheck(newToken.refreshToken());
            redisTemplate.opsForValue().set(REFRESH_TOKEN+authentication.getName(), newToken.refreshToken(), Duration.ofSeconds(ttl));
            return newToken;
        }

        // 3일 이상 → access만 재발급
        return jwtProvider.refreshAccessToken(authentication, refreshToken);
    }

    public void addBlacklistToken(String refreshToken, String loginId) {
        long ttl = jwtProvider.getExpirationTimeInSeconds(refreshToken);

        redisTemplate.opsForValue().set(BLACKLISTED_TOKEN+refreshToken, refreshToken, Duration.ofSeconds(ttl));
        redisTemplate.delete(REFRESH_TOKEN+loginId);
    }

    public void validateRefreshTokenOwnerId(String refreshToken, String ownerId) {
        String loginId = jwtProvider.getLoginId(refreshToken);
        if (!loginId.equals(ownerId)) {
            throw new UnauthenticatedException(ErrorCode.INVALID_TOKEN_OWNER);
        }
    }

    public void validateLogoutToken(String refreshToken) {
        boolean isBlacklisted = Boolean.TRUE.equals(
                redisTemplate.hasKey(BLACKLISTED_TOKEN + refreshToken)
        );

        if (isBlacklisted) {
            throw new UnauthenticatedException(ErrorCode.INVALID_REFRESH_TOKEN);
        }
    }
}
