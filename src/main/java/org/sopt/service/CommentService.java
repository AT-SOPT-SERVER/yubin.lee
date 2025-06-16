package org.sopt.service;

import lombok.RequiredArgsConstructor;
import org.sopt.domain.Comment;
import org.sopt.domain.Post;
import org.sopt.domain.User;
import org.sopt.dto.request.CommentRequestDto;
import org.sopt.dto.response.CommentResponse;
import org.sopt.exception.CustomAccessDeniedException;
import org.sopt.exception.ErrorCode;
import org.sopt.repository.CommentRepository;
import org.sopt.repository.PostRepository;
import org.sopt.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @Transactional
    public void createComment(Long userId, Long postId, CommentRequestDto requestDto) {
        User user = userRepository.findById(userId).orElseThrow();
        Post post = postRepository.findById(postId).orElseThrow();

        Comment comment = Comment.builder()
                .comment(requestDto.comment())
                .user(user)
                .post(post)
                .build();

        commentRepository.save(comment);
    }

    @Transactional
    public void updateComment(Long userId, Long postId, Long commentId, CommentRequestDto requestDto) {
        Comment comment = commentRepository.findByIdAndUserIdAndPostId(commentId, userId, postId)
                .orElseThrow(() -> new CustomAccessDeniedException(ErrorCode.COMMENT_WRITE_DENIED));

        comment.updateComment(requestDto.comment());
    }

    @Transactional
    public void deleteComment(Long userId, Long postId, Long commentId) {
        Comment comment = commentRepository.findByIdAndUserIdAndPostId(commentId, userId, postId)
                .orElseThrow(() -> new CustomAccessDeniedException(ErrorCode.COMMENT_DELETE_DENIED));

        commentRepository.delete(comment);

    }
    @Transactional(readOnly = true)
    public List<CommentResponse> getComments(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow();

        return commentRepository.findByPost(post)
                .stream()
                .map(CommentResponse::from)
                .toList();
    }

}
