package org.sopt.domain.like.controller;

import lombok.RequiredArgsConstructor;
import org.sopt.domain.like.service.PostCommentLikeService;
import org.sopt.domain.user.dto.jwt.UserDetails;
import org.sopt.global.enums.ResponseMessage;
import org.sopt.global.dto.response.SuccessResponse;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/likes/comments/{commentId}")
public class PostCommentLikeController {

    private final PostCommentLikeService postCommentLikeService;

    @GetMapping
    public SuccessResponse<Long> getPostCommentLikes(@PathVariable("commentId") long commentId){
        long count = postCommentLikeService.countPostCommentLikes(commentId);
        return new SuccessResponse<>(ResponseMessage.COUNT_COMMENT_LIKE_SUCCESS.getMessage(), count);
    }

    @PostMapping
    public SuccessResponse<String> addPostCommentLike(@AuthenticationPrincipal UserDetails userDetails,
                                                                      @PathVariable("commentId") long commentId){
        postCommentLikeService.addPostCommentLike(userDetails.userId(), commentId);
        return new SuccessResponse<>(ResponseMessage.CREATE_POST_COMMENT_LIKE_SUCCESS.getMessage());
    }

    @DeleteMapping
    public SuccessResponse<String> deletePostCommentLike(@AuthenticationPrincipal UserDetails userDetails,
                                                                         @PathVariable("commentId") long commentId){
        postCommentLikeService.removePostCommentLike(userDetails.userId(), commentId);
        return new SuccessResponse<>(ResponseMessage.CANCEL_POST_COMMENT_LIKE_SUCCESS.getMessage());
    }
}
