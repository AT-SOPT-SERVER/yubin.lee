package org.sopt.service;

import org.sopt.domain.User;
import org.sopt.dto.request.UserCreateRequest;
import org.sopt.dto.response.SuccessResponse;
import org.sopt.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public SuccessResponse saveUser(UserCreateRequest userCreateRequest){
        userRepository.save(new User(userCreateRequest.name(), userCreateRequest.email()));
        return new SuccessResponse("유저 추가를 완료했습니다.");
    }
}
