package org.sopt.domain.comment.service;

import lombok.RequiredArgsConstructor;
import org.sopt.domain.comment.domain.PostComment;
import org.sopt.domain.post.model.Post;
import org.sopt.domain.user.model.User;
import org.sopt.domain.comment.dto.request.CommentRequestDto;
import org.sopt.domain.comment.dto.response.CommentResponse;
import org.sopt.global.exception.CustomAccessDeniedException;
import org.sopt.global.ErrorCode;
import org.sopt.domain.comment.repository.PostCommentRepository;
import org.sopt.domain.post.repository.PostRepository;
import org.sopt.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final PostCommentRepository postCommentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @Transactional
    public void createComment(Long userId, Long postId, CommentRequestDto requestDto) {
        User user = userRepository.findById(userId).orElseThrow();
        Post post = postRepository.findById(postId).orElseThrow();

        PostComment postComment = PostComment.builder()
                .comment(requestDto.comment())
                .user(user)
                .post(post)
                .build();

        postCommentRepository.save(postComment);
    }

    @Transactional
    public void updateComment(Long userId, Long postId, Long commentId, CommentRequestDto requestDto) {
        PostComment postComment = postCommentRepository.findByIdAndUserIdAndPostId(commentId, userId, postId)
                .orElseThrow(() -> new CustomAccessDeniedException(ErrorCode.COMMENT_WRITE_DENIED));

        postComment.updateComment(requestDto.comment());
    }

    @Transactional
    public void deleteComment(Long userId, Long postId, Long commentId) {
        PostComment postComment = postCommentRepository.findByIdAndUserIdAndPostId(commentId, userId, postId)
                .orElseThrow(() -> new CustomAccessDeniedException(ErrorCode.COMMENT_DELETE_DENIED));

        postCommentRepository.delete(postComment);

    }
    @Transactional(readOnly = true)
    public List<CommentResponse> getComments(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow();

        return postCommentRepository.findByPost(post)
                .stream()
                .map(CommentResponse::from)
                .toList();
    }
}
