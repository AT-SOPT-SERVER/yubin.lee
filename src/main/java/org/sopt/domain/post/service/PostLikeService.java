package org.sopt.domain.post.service;

import lombok.RequiredArgsConstructor;
import org.sopt.domain.like.dto.response.UsersWhoLikedPostDto;
import org.sopt.domain.like.model.PostLike;
import org.sopt.domain.like.repository.PostLikeRepository;
import org.sopt.domain.post.model.Post;
import org.sopt.domain.post.repository.PostRepository;
import org.sopt.domain.user.model.User;
import org.sopt.domain.user.repository.UserRepository;
import org.sopt.global.ErrorCode;
import org.sopt.global.exception.CustomBadRequestException;
import org.sopt.global.exception.CustomNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PostLikeService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final PostLikeRepository postLikeRepository;

    @Transactional
    public void addPostLike(long userId, long postId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomNotFoundException(ErrorCode.NOT_FOUND_USER));
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomNotFoundException(ErrorCode.NOT_FOUND_POST));

        if (postLikeRepository.existsByUserIdAndPostId(userId, postId)) {
            throw new CustomBadRequestException(ErrorCode.ALREADY_LIKED_POST);
        }

        PostLike postLike = PostLike.builder()
                .user(user)
                .post(post)
                .build();

        postLikeRepository.save(postLike);
    }

    @Transactional
    public void removePostLike(long userId, long postId) {
        PostLike postLike = postLikeRepository.findByUserIdAndPostId(userId, postId)
                .orElseThrow(()-> new CustomNotFoundException(ErrorCode.NOT_FOUND_POST_LIKE));

        postLikeRepository.delete(postLike);
    }

    @Transactional(readOnly = true)
    public long countPostLikes(long postId) {
        return postLikeRepository.countByPostId(postId);
    }

    @Transactional(readOnly = true)
    public List<UsersWhoLikedPostDto> getUsersWhoLikedPost(Long postId) {
        return postLikeRepository.findUsersWhoLikedPost(postId)
                .stream().map(UsersWhoLikedPostDto::from).toList();
    }
}
