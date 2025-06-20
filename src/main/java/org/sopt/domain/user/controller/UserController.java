package org.sopt.domain.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.sopt.domain.user.dto.request.UserCreateRequest;
import org.sopt.domain.user.dto.request.UserLoginRequest;
import org.sopt.domain.user.dto.response.TokenDto;
import org.sopt.global.enums.ResponseMessage;
import org.sopt.global.dto.response.SuccessResponse;
import org.sopt.domain.user.service.UserService;
import org.sopt.domain.user.service.TokenService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final TokenService tokenService;

    @PostMapping("/signup")
    public SuccessResponse<String> createUser(@RequestBody @Valid UserCreateRequest userCreateRequest){
        userService.join(userCreateRequest);
        return new SuccessResponse<>(ResponseMessage.CREATE_USER_SUCCESS.getMessage());
    }

    @PostMapping("/login")
    public ResponseEntity<SuccessResponse<TokenDto>> login(@RequestBody @Valid UserLoginRequest userLoginRequest){
        TokenDto tokenDto = userService.login(userLoginRequest);

        ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", tokenDto.refreshToken())
                .httpOnly(true)
                //.secure(true) // HTTPS
                .path("/users")
                .maxAge(Duration.ofDays(7))
                .sameSite("Lax")
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                .body(new SuccessResponse<>(ResponseMessage.LOGIN_SUCCESS.getMessage(), tokenDto));
    }

    @PostMapping("/reissue")
    public ResponseEntity<SuccessResponse<TokenDto>> reissue(@CookieValue("refreshToken") String refreshToken){
        TokenDto tokenDto = userService.reissueToken(refreshToken);

        ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", tokenDto.refreshToken())
                .httpOnly(true)
                //.secure(true)
                .path("/users")
                .maxAge(Duration.ofDays(7))
                .sameSite("Lax")
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                .body(new SuccessResponse<>(ResponseMessage.TOKEN_REFRESH_SUCCESS.getMessage(), tokenDto));
    }

    @PostMapping("/logout")
    public ResponseEntity<SuccessResponse<String>> logout(@CookieValue("refreshToken") String refreshToken){
        tokenService.addBlacklistToken(refreshToken);

        ResponseCookie deleteCookie = ResponseCookie.from("refreshToken", "")
                .httpOnly(true)
                //.secure(true)
                .path("/users")
                .maxAge(0)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, deleteCookie.toString())
                .body(new SuccessResponse<>(ResponseMessage.LOGOUT_SUCCESS.getMessage()));
    }
}
