package org.sopt.domain.user.controller;

import org.sopt.domain.user.dto.request.UserCreateRequest;
import org.sopt.global.ResponseMessage;
import org.sopt.global.dto.SuccessResponse;
import org.sopt.domain.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<SuccessResponse<String>> createUser(@RequestBody UserCreateRequest userCreateRequest){
        userService.saveUser(userCreateRequest);
        return ResponseEntity.ok(new SuccessResponse<>(ResponseMessage.CREATE_USER_SUCCESS.getMessage()));
    }
}
