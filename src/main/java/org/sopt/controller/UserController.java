package org.sopt.controller;

import org.sopt.dto.request.UserCreateRequest;
import org.sopt.dto.response.SuccessResponse;
import org.sopt.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @PostMapping("/users")
    public ResponseEntity<SuccessResponse> createUser(@RequestBody UserCreateRequest userCreateRequest){
        SuccessResponse successResponse = userService.saveUser(userCreateRequest);
        return ResponseEntity.ok(successResponse);
    }
}
