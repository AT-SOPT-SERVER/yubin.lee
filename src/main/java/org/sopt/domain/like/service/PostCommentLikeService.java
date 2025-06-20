package org.sopt.domain.like.service;

import lombok.RequiredArgsConstructor;
import org.sopt.domain.like.domain.PostCommentLike;
import org.sopt.domain.like.repository.PostCommentLikeRepository;
import org.sopt.domain.comment.domain.PostComment;
import org.sopt.domain.comment.repository.PostCommentRepository;
import org.sopt.domain.user.domain.User;
import org.sopt.domain.user.repository.UserRepository;
import org.sopt.global.enums.ErrorCode;
import org.sopt.global.exception.CustomBadRequestException;
import org.sopt.global.exception.CustomNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostCommentLikeService {

    private final UserRepository userRepository;
    private final PostCommentRepository postCommentRepository;
    private final PostCommentLikeRepository postCommentLikeRepository;

    @Transactional
    public void addPostCommentLike(long userId, long commentId) {
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new CustomNotFoundException(ErrorCode.NOT_FOUND_USER));
        PostComment postComment = postCommentRepository.findById(commentId)
                .orElseThrow(() -> new CustomNotFoundException(ErrorCode.NOT_FOUND_COMMENT));

        if (postCommentLikeRepository.existsByUserIdAndPostCommentId(userId, commentId)){
            throw new CustomBadRequestException(ErrorCode.ALREADY_LIKED);
        }

        PostCommentLike postCommentLike = PostCommentLike.builder()
                .user(user)
                .postComment(postComment)
                .build();

        postCommentLikeRepository.save(postCommentLike);
    }

    @Transactional
    public void removePostCommentLike(long userId, long commentId) {
        PostCommentLike postCommentLike = postCommentLikeRepository.findByUserIdAndPostCommentId(userId, commentId).orElseThrow(
                () -> new CustomNotFoundException(ErrorCode.NOT_FOUND_POST_LIKE)
        );

        postCommentLikeRepository.delete(postCommentLike);
    }

    @Transactional(readOnly = true)
    public long countPostCommentLikes(long commentId) {
        return postCommentLikeRepository.countByPostCommentId(commentId);
    }
}
