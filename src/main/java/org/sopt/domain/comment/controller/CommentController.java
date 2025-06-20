package org.sopt.domain.comment.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.sopt.domain.comment.service.CommentService;
import org.sopt.domain.comment.dto.request.CommentRequestDto;
import org.sopt.domain.comment.dto.response.CommentResponse;
import org.sopt.domain.user.dto.jwt.UserDetails;
import org.sopt.global.enums.ResponseMessage;
import org.sopt.global.dto.response.SuccessResponse;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("posts/{postId}/comments")
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public SuccessResponse<String> addComment(@AuthenticationPrincipal UserDetails userDetails,
                                                              @PathVariable("postId") Long postId,
                                                              @RequestBody @Valid final CommentRequestDto commentRequestDto){
        commentService.createComment(userDetails.userId(), postId, commentRequestDto);
        return new SuccessResponse<>(ResponseMessage.CREATE_COMMENT_SUCCESS.getMessage());
    }

    @PatchMapping("/{commentId}")
    public SuccessResponse<String> updateComment(@AuthenticationPrincipal UserDetails userDetails,
                                                                 @PathVariable("postId") long postId,
                                                                 @PathVariable("commentId") long commentId,
                                                                 @RequestBody @Valid final CommentRequestDto commentRequestDto){
        commentService.updateComment(userDetails.userId(), postId, commentId, commentRequestDto);
        return new SuccessResponse<>(ResponseMessage.UPDATE_COMMENT_SUCCESS.getMessage());
    }

    @DeleteMapping("/{commentId}")
    public SuccessResponse<String> deleteComment(@AuthenticationPrincipal UserDetails userDetails,
                                                                 @PathVariable("postId") long postId,
                                                                 @PathVariable("commentId") long commentId){
        commentService.deleteComment(userDetails.userId(), postId, commentId);
        return new SuccessResponse<>(ResponseMessage.DELETE_COMMENT_SUCCESS.getMessage());
    }

    @GetMapping
    public SuccessResponse<List<CommentResponse>> getComments(@PathVariable("postId") long postId){
        List<CommentResponse> response = commentService.getComments(postId);
        return new SuccessResponse<>(ResponseMessage.GET_COMMENT_SUCCESS.getMessage(), response);
    }
}
