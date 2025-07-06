package org.sopt.domain.like.service;

import org.sopt.domain.like.domain.PostCommentLike;
import org.sopt.domain.like.dto.response.UsersWhoLikedPostDto;
import org.sopt.domain.like.repository.PostCommentLikeRepository;
import org.sopt.domain.comment.domain.PostComment;
import org.sopt.domain.comment.repository.PostCommentRepository;
import org.sopt.domain.user.domain.User;
import org.sopt.domain.user.repository.UserRepository;
import org.sopt.global.enums.ErrorCode;
import org.sopt.global.exception.CustomBadRequestException;
import org.sopt.global.exception.CustomNotFoundException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PostCommentLikeService {

    private final UserRepository userRepository;
    private final PostCommentRepository postCommentRepository;
    private final PostCommentLikeRepository postCommentLikeRepository;
    private final RedisTemplate<String, String> commentLikeCacheRedisTemplate;

    public PostCommentLikeService(UserRepository userRepository,
                                  PostCommentRepository postCommentRepository,
                                  PostCommentLikeRepository postCommentLikeRepository,
                                  @Qualifier("commentLikeCacheRedisTemplate")
                                  RedisTemplate<String, String> commentLikeCacheRedisTemplate
    ) {
        this.userRepository = userRepository;
        this.postCommentRepository = postCommentRepository;
        this.postCommentLikeRepository = postCommentLikeRepository;
        this.commentLikeCacheRedisTemplate = commentLikeCacheRedisTemplate;
    }

    private static final String COMMENT_LIKE_SET_KEY = "commentLike:set:";
    private static final Duration LIKE_CACHE_TTL = Duration.ofMinutes(10);

    @Transactional
    public void addPostCommentLike(long userId, long commentId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomNotFoundException(ErrorCode.NOT_FOUND_USER));
        PostComment postComment = postCommentRepository.findById(commentId)
                .orElseThrow(() -> new CustomNotFoundException(ErrorCode.NOT_FOUND_COMMENT));

        if (postCommentLikeRepository.existsByUserIdAndPostCommentId(userId, commentId)) {
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
        PostCommentLike postCommentLike = postCommentLikeRepository.findByUserIdAndPostCommentId(userId, commentId)
                .orElseThrow(() -> new CustomNotFoundException(ErrorCode.NOT_FOUND_POST_LIKE));

        postCommentLikeRepository.delete(postCommentLike);

        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                String key = COMMENT_LIKE_SET_KEY + commentId;
                commentLikeCacheRedisTemplate.opsForSet().remove(key, String.valueOf(userId));
                commentLikeCacheRedisTemplate.expire(key, LIKE_CACHE_TTL);
            }
        });
    }

    @Transactional(readOnly = true)
    public long countPostCommentLikes(long commentId, long userId) {
        String key = COMMENT_LIKE_SET_KEY + commentId;
        Long size = commentLikeCacheRedisTemplate.opsForSet().size(key);
        if (size != null) {
            return size;
        }

        commentLikeCacheRedisTemplate.opsForSet().add(key, String.valueOf(userId));
        commentLikeCacheRedisTemplate.expire(key, LIKE_CACHE_TTL);

        return postCommentLikeRepository.countByPostCommentId(commentId);
    }

    @Transactional(readOnly = true)
    public List<UsersWhoLikedPostDto> getUsersWhoLikedComment(long commentId) {
        String key = COMMENT_LIKE_SET_KEY + commentId;
        Set<String> cached = commentLikeCacheRedisTemplate.opsForSet().members(key);

        if (cached != null && !cached.isEmpty()) {
            return cached.stream()
                    .map(id -> userRepository.findById(Long.parseLong(id)))
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .map(UsersWhoLikedPostDto::from)
                    .collect(Collectors.toList());
        }

        List<User> users = postCommentLikeRepository.findUsersByPostCommentId(commentId);
        if (users.isEmpty()) return Collections.emptyList();

        Set<String> userIdStrings = users.stream()
                .map(user -> String.valueOf(user.getId()))
                .collect(Collectors.toSet());

        commentLikeCacheRedisTemplate.opsForSet().add(key, userIdStrings.toArray(new String[0]));
        commentLikeCacheRedisTemplate.expire(key, LIKE_CACHE_TTL);

        return users.stream()
                .map(UsersWhoLikedPostDto::from)
                .collect(Collectors.toList());
    }
}