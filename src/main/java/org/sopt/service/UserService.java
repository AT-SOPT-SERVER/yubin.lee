package org.sopt.service;

import org.sopt.domain.Post;
import org.sopt.domain.User;
import org.sopt.dto.request.UserCreateRequest;
import org.sopt.dto.response.SuccessResponse;
import org.sopt.exception.CustomAccessDeniedException;
import org.sopt.exception.UnauthenticatedException;
import org.sopt.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

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

    public User existsUser(Long userId){
        return userRepository.findById(userId).orElseThrow(() -> new UnauthenticatedException("존재하지 않는 사용자입니다."));
    }

    public void validatePostOwnership(Post post, User user) {
        if (!post.getUser().getId().equals(user.getId())) {
            throw new CustomAccessDeniedException("게시물에 대한 접근 권한이 없습니다.");
        }
    }
}
