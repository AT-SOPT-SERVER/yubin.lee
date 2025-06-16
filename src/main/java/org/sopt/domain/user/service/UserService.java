package org.sopt.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.sopt.domain.post.model.Post;
import org.sopt.domain.user.model.User;
import org.sopt.domain.user.dto.request.UserCreateRequest;
import org.sopt.global.exception.CustomAccessDeniedException;
import org.sopt.global.ErrorCode;
import org.sopt.global.exception.UnauthenticatedException;
import org.sopt.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public void saveUser(UserCreateRequest userCreateRequest){
        userRepository.save(new User(userCreateRequest.name(), userCreateRequest.email()));
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
