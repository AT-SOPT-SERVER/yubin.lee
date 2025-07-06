package org.sopt.domain.user.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.sopt.domain.user.dto.jwt.UserDetails;
import org.sopt.domain.user.dto.response.TokenDto;
import org.sopt.domain.user.domain.User;
import org.sopt.domain.user.repository.UserRepository;
import org.sopt.global.enums.ErrorCode;
import org.sopt.global.exception.UnauthenticatedException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class JwtProvider {

    private final Key key;
    private final UserRepository userRepository;
    private static final long ACCESS_TOKEN_EXPIRED_TIME = 1000L * 60 * 3;            // 30분
    private static final long REFRESH_TOKEN_EXPIRED_TIME = 1000L * 60 * 60 * 24 * 7;  // 7일
    private static final String AUTHORITIES_KEY = "auth";
    private static final String USER_KEY = "user";
    private static final String BEARER_TYPE = "Bearer";

    public JwtProvider(@Value("${jwt.token}") String secret, UserRepository userRepository) {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.userRepository = userRepository;
    }

    public TokenDto generateToken(Authentication authentication) {
        long nowTime = (new Date()).getTime();
        String accessToken = createAccessToken(authentication, nowTime);
        String refreshToken = createRefreshToken(authentication, nowTime);

        return TokenDto.of(BEARER_TYPE, accessToken, refreshToken, nowTime + ACCESS_TOKEN_EXPIRED_TIME);
    }

    public TokenDto refreshAccessToken(Authentication authentication, String refreshToken) {
        long nowTime = (new Date()).getTime();
        String accessToken = createAccessToken(authentication, nowTime);

        return TokenDto.of(BEARER_TYPE, accessToken, refreshToken, nowTime + ACCESS_TOKEN_EXPIRED_TIME);
    }

    public String createAccessToken(Authentication authentication, long nowTime) {
        String roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        UserDetails user = (UserDetails) authentication.getPrincipal();

        return Jwts.builder()
                .setSubject(user.loginId())
                .claim(USER_KEY, user.userId())
                .claim(AUTHORITIES_KEY, roles)
                .setExpiration(new Date(nowTime + ACCESS_TOKEN_EXPIRED_TIME))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    private String createRefreshToken(Authentication authentication, long nowTime) {
        String roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        UserDetails user = (UserDetails) authentication.getPrincipal();

        return Jwts.builder()
                .setSubject(user.loginId())
                .claim(AUTHORITIES_KEY, roles)
                .setExpiration(new Date(nowTime + REFRESH_TOKEN_EXPIRED_TIME))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    public Authentication getAuthentication(String refreshToken) {
        Claims claims = parseClaims(refreshToken);

        if (claims.get(AUTHORITIES_KEY) == null) {
            throw new UnauthenticatedException(ErrorCode.NOT_FOUND_AUTHORITY);
        }

        List<SimpleGrantedAuthority> authorities = Arrays.stream(
                        claims.get(AUTHORITIES_KEY).toString().split(","))
                .map(SimpleGrantedAuthority::new)
                .toList();

        String loginId = claims.getSubject();

        User user = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new UnauthenticatedException(ErrorCode.NOT_FOUND_USER));

        UserDetails principal = UserDetails.of(user.getLoginId(), user.getId()); // or of(user.getLoginId(), user.getId())

        return new JwtAuthentication(principal, null, authorities);
    }

    public long refreshTokenPeriodCheck(String token) {
        Claims claims = parseClaims(token);

        long now = (new Date()).getTime();
        long expiration = claims.getExpiration().getTime();

        // 만료까지 남은 시간
        return expiration - now;
    }

    private Claims parseClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        } catch (JwtException | IllegalArgumentException e) {
            throw new UnauthenticatedException(ErrorCode.INVALID_REFRESH_TOKEN);
        }
    }

    public UserDetails getUserDetails(String accessToken) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(accessToken)
                    .getBody();

            return new UserDetails(claims.getSubject(), claims.get(USER_KEY, Long.class));
        } catch (ExpiredJwtException e) {
            throw new UnauthenticatedException(ErrorCode.EXPIRED_TOKEN);
        } catch (UnsupportedJwtException e) {
            throw new UnauthenticatedException(ErrorCode.UN_SUPPORTED_TOKEN);
        } catch(io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            throw new UnauthenticatedException(ErrorCode.INVALID_SIGNATURE);
        } catch (IllegalArgumentException e) {
            throw new UnauthenticatedException(ErrorCode.INVALID_TOKEN);
        }
    }

    public String getLoginId(String refreshToken) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(refreshToken)
                    .getBody();

            return claims.getSubject();
        } catch (ExpiredJwtException e) {
            throw new UnauthenticatedException(ErrorCode.EXPIRED_TOKEN);
        } catch (UnsupportedJwtException e) {
            throw new UnauthenticatedException(ErrorCode.UN_SUPPORTED_TOKEN);
        } catch(io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            throw new UnauthenticatedException(ErrorCode.INVALID_SIGNATURE);
        } catch (IllegalArgumentException e) {
            throw new UnauthenticatedException(ErrorCode.INVALID_TOKEN);
        }
    }

    public List<GrantedAuthority> getAuthorities(String token) {
        Claims claims = parseClaims(token);

        String roles = claims.get("auth", String.class); // "ROLE_USER,ROLE_ADMIN"
        if (roles == null || roles.isEmpty()) {
            return Collections.emptyList();
        }

        return Arrays.stream(roles.split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            throw new UnauthenticatedException(ErrorCode.EXPIRED_TOKEN);
        } catch (UnsupportedJwtException e) {
            throw new UnauthenticatedException(ErrorCode.UN_SUPPORTED_TOKEN);
        } catch (SecurityException | MalformedJwtException e) {
            throw new UnauthenticatedException(ErrorCode.INVALID_SIGNATURE);
        } catch (IllegalArgumentException e) {
            throw new UnauthenticatedException(ErrorCode.INVALID_TOKEN);
        }
    }

    public long getExpirationTimeInSeconds(String token) {
        Claims claims = parseClaims(token);
        Date expiration = claims.getExpiration();
        long expirationTimeMillis = expiration.getTime();
        long nowMillis = System.currentTimeMillis();
        long ttlInMillis = expirationTimeMillis - nowMillis;

        return Math.max(ttlInMillis / 1000, 0);
    }
}