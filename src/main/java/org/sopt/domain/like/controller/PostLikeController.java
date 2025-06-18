package org.sopt.domain.like.controller;

import lombok.RequiredArgsConstructor;
import org.sopt.domain.like.dto.response.UsersWhoLikedPostDto;
import org.sopt.domain.like.service.PostLikeService;
import org.sopt.global.ResponseMessage;
import org.sopt.global.dto.SuccessResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/likes/posts/{postId}")
public class PostLikeController {

    private final PostLikeService postLikeService;

    @GetMapping
    public ResponseEntity<SuccessResponse<Long>> getPostLikes(@RequestHeader("userId") long userId,
                                                              @PathVariable("postId") long postId){
        long counts = postLikeService.countPostLikes(postId);
        return ResponseEntity.ok(new SuccessResponse<>(counts));
    }

    @PostMapping
    public ResponseEntity<SuccessResponse<String>> addPostLike(@RequestHeader("userId") long userId,
                                                               @PathVariable("postId") long postId){
        postLikeService.addPostLike(userId, postId);
        return ResponseEntity.ok(new SuccessResponse<>(ResponseMessage.CREATE_POSTLIKE_SUCCESS.getMessage()));
    }

    @DeleteMapping
    public ResponseEntity<SuccessResponse<String>> deletePostLike(@RequestHeader("userId") long userId,
                                                                  @PathVariable("postId") long postId){
        postLikeService.removePostLike(userId, postId);
        return ResponseEntity.ok(new SuccessResponse<>(ResponseMessage.CANCEL_POSTLIKE_SUCCESS.getMessage()));
    }

    @GetMapping
    public ResponseEntity<SuccessResponse<List<UsersWhoLikedPostDto>>> getLikedPostsUserList(@RequestHeader("userId") long userId,
                                                                                             @PathVariable("postId") long postId){
        List<UsersWhoLikedPostDto> users = postLikeService.getUsersWhoLikedPost(postId);
        return ResponseEntity.ok(new SuccessResponse<>(users));
    }
}
