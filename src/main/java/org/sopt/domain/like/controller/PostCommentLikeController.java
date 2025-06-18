package org.sopt.domain.like.controller;

import lombok.RequiredArgsConstructor;
import org.sopt.domain.like.service.PostCommentLikeService;
import org.sopt.global.ResponseMessage;
import org.sopt.global.dto.response.SuccessResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/likes/comments/{commentId}")
public class PostCommentLikeController {

    private final PostCommentLikeService postCommentLikeService;

    @GetMapping
    public SuccessResponse<Long> getPostCommentLikes(@PathVariable("commentId") long commentId){
        long count = postCommentLikeService.countPostCommentLikes(commentId);
        return new SuccessResponse<>(count);
    }

    @PostMapping
    public SuccessResponse<String> addPostCommentLike(@RequestHeader("userId") long userId,
                                                                      @PathVariable("commentId") long commentId){
        postCommentLikeService.addPostCommentLike(userId, commentId);
        return new SuccessResponse<>(ResponseMessage.CREATE_POST_COMMENT_LIKE_SUCCESS.getMessage());
    }

    @DeleteMapping
    public SuccessResponse<String> deletePostCommentLike(@RequestHeader("userId") long userId,
                                                                         @PathVariable("commentId") long commentId){
        postCommentLikeService.removePostCommentLike(userId, commentId);
        return new SuccessResponse<>(ResponseMessage.CANCEL_POST_COMMENT_LIKE_SUCCESS.getMessage());
    }
}
