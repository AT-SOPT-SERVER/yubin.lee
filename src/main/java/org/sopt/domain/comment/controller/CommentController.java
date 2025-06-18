package org.sopt.domain.comment.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.sopt.domain.comment.service.CommentService;
import org.sopt.domain.comment.dto.request.CommentRequestDto;
import org.sopt.domain.comment.dto.response.CommentResponse;
import org.sopt.global.ResponseMessage;
import org.sopt.global.dto.SuccessResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("posts/{postId}/comments")
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<SuccessResponse<String>> addComment(@RequestHeader("userId") Long userId,
                                                              @PathVariable("postId") Long postId,
                                                              @RequestBody @Valid final CommentRequestDto commentRequestDto){
        commentService.createComment(userId, postId, commentRequestDto);
        return ResponseEntity.ok(new SuccessResponse<>(ResponseMessage.CREATE_COMMENT_SUCCESS.getMessage()));
    }

    @PatchMapping("/{commentId}")
    public ResponseEntity<SuccessResponse<String>> updateComment(@RequestHeader("userId") long userId,
                                                                 @PathVariable("postId") long postId,
                                                                 @PathVariable("commentId") long commentId,
                                                                 @RequestBody @Valid final CommentRequestDto commentRequestDto){
        commentService.updateComment(userId, postId, commentId, commentRequestDto);
        return ResponseEntity.ok(new SuccessResponse<>(ResponseMessage.UPDATE_COMMENT_SUCCESS.getMessage()));
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<SuccessResponse<String>> deleteComment(@RequestHeader("userId") long userId,
                                                                 @PathVariable("postId") long postId,
                                                                 @PathVariable("commentId") long commentId){
        commentService.deleteComment(userId, postId, commentId);
        return ResponseEntity.ok(new SuccessResponse<>(ResponseMessage.DELETE_COMMENT_SUCCESS.getMessage()));
    }

    @GetMapping
    public ResponseEntity<SuccessResponse<List<CommentResponse>>> getComments(@PathVariable("postId") long postId){
        List<CommentResponse> response = commentService.getComments(postId);
        return ResponseEntity.ok(new SuccessResponse<>(response));
    }
}
