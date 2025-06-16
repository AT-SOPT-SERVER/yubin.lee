package org.sopt.service;

import lombok.RequiredArgsConstructor;
import org.sopt.domain.Post;
import org.sopt.domain.User;
import org.sopt.dto.request.UserCreateRequest;
import org.sopt.exception.CustomAccessDeniedException;
import org.sopt.exception.ErrorCode;
import org.sopt.exception.UnauthenticatedException;
import org.sopt.repository.UserRepository;
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
