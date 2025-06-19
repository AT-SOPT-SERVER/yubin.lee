package org.sopt.domain.user.controller;

import jakarta.validation.Valid;
import org.sopt.domain.user.dto.request.TokenRequest;
import org.sopt.domain.user.dto.request.UserCreateRequest;
import org.sopt.domain.user.dto.request.UserLoginRequest;
import org.sopt.domain.user.dto.response.TokenDto;
import org.sopt.global.ResponseMessage;
import org.sopt.global.dto.response.SuccessResponse;
import org.sopt.domain.user.service.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @PostMapping("/signup")
    public SuccessResponse<String> createUser(@RequestBody @Valid UserCreateRequest userCreateRequest){
        userService.join(userCreateRequest);
        return new SuccessResponse<>(ResponseMessage.CREATE_USER_SUCCESS.getMessage());
    }

    @PostMapping("/login")
    public ResponseEntity<SuccessResponse<TokenDto>> login(@RequestBody UserLoginRequest userLoginRequest){
        TokenDto response = userService.login(userLoginRequest);

        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, response.grantType() + " " + response.accessToken())
                .header("Refresh-Token", response.refreshToken())
                .body(new SuccessResponse<>(response));
    }

    @PostMapping("/reissue")
    public SuccessResponse<TokenDto> reissue(@RequestBody TokenRequest tokenRequest){
        TokenDto response = userService.reissueToken(tokenRequest);
        return new SuccessResponse<>(response);
    }
}
