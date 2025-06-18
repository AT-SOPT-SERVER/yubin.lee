package org.sopt.domain.like.controller;

import lombok.RequiredArgsConstructor;
import org.sopt.domain.like.dto.response.UsersWhoLikedPostDto;
import org.sopt.domain.like.service.PostLikeService;
import org.sopt.global.ResponseMessage;
import org.sopt.global.dto.response.SuccessResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/likes/posts/{postId}")
public class PostLikeController {

    private final PostLikeService postLikeService;

    @GetMapping
    public SuccessResponse<Long> getPostLikes(@RequestHeader("userId") long userId,
                                                              @PathVariable("postId") long postId){
        long counts = postLikeService.countPostLikes(postId);
        return new SuccessResponse<>(counts);
    }

    @PostMapping
    public SuccessResponse<String> addPostLike(@RequestHeader("userId") long userId,
                                                               @PathVariable("postId") long postId){
        postLikeService.addPostLike(userId, postId);
        return new SuccessResponse<>(ResponseMessage.CREATE_POSTLIKE_SUCCESS.getMessage());
    }

    @DeleteMapping
    public SuccessResponse<String> deletePostLike(@RequestHeader("userId") long userId,
                                                                  @PathVariable("postId") long postId){
        postLikeService.removePostLike(userId, postId);
        return new SuccessResponse<>(ResponseMessage.CANCEL_POSTLIKE_SUCCESS.getMessage());
    }

    @GetMapping("/users")
    public SuccessResponse<List<UsersWhoLikedPostDto>> getLikedPostsUserList(@RequestHeader("userId") long userId,
                                                                                             @PathVariable("postId") long postId){
        List<UsersWhoLikedPostDto> users = postLikeService.getUsersWhoLikedPost(postId);
        return new SuccessResponse<>(users);
    }
}
