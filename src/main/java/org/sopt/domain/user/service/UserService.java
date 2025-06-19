package org.sopt.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.sopt.domain.post.model.Post;
import org.sopt.domain.user.dto.jwt.ValidatedTokenResult;
import org.sopt.domain.user.dto.request.TokenRequest;
import org.sopt.domain.user.dto.request.UserLoginRequest;
import org.sopt.domain.user.dto.response.TokenDto;
import org.sopt.domain.user.model.RefreshToken;
import org.sopt.domain.user.model.Role;
import org.sopt.domain.user.model.User;
import org.sopt.domain.user.dto.request.UserCreateRequest;
import org.sopt.global.exception.CustomAccessDeniedException;
import org.sopt.global.ErrorCode;
import org.sopt.global.exception.CustomNotFoundException;
import org.sopt.global.exception.UnauthenticatedException;
import org.sopt.domain.user.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    @Transactional
    public void join(UserCreateRequest userCreateRequest){
        User user = User.builder()
                .loginId(userCreateRequest.loginId())
                .email(userCreateRequest.email())
                .name(userCreateRequest.name())
                .password(passwordEncoder.encode(userCreateRequest.password()))
                .role(Role.ROLE_USER)
                .build();

        userRepository.save(user);
    }

    public TokenDto login(UserLoginRequest userLoginRequest) {
        User user = userRepository.findByLoginId(userLoginRequest.loginId())
                .orElseThrow(() -> new CustomNotFoundException(ErrorCode.NOT_FOUND_USER));

        if (!passwordEncoder.matches(userLoginRequest.password(), user.getPassword())) {
            throw new UnauthenticatedException(ErrorCode.INVALID_PASSWORD);
        }

        return tokenService.generateAndSaveToken(user);
    }

    public TokenDto reissueToken(TokenRequest tokenRequest) {
        ValidatedTokenResult result = tokenService.validateRefreshToken(tokenRequest.accessToken(), tokenRequest.refreshToken());
        RefreshToken refreshToken = result.refreshToken();
        Authentication authentication = result.authentication();

        if (!refreshToken.getValue().equals(tokenRequest.refreshToken())) {
            throw new UnauthenticatedException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        return tokenService.generateNewRefreshToken(authentication, refreshToken);
    }

    public User existsUser(Long userId){
        return userRepository.findById(userId).orElseThrow(() -> new UnauthenticatedException(ErrorCode.UN_AUTHENTICATION));
    }

    public void validatePostOwnership(Post post, User user) {
        if (!post.getUser().getId().equals(user.getId())) {
            throw new CustomAccessDeniedException(ErrorCode.POST_ACCESS_DENIED);
        }
    }
}
