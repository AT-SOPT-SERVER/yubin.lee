package org.sopt.domain.like.controller;

import lombok.RequiredArgsConstructor;
import org.sopt.domain.like.dto.response.UsersWhoLikedPostDto;
import org.sopt.domain.like.service.PostLikeService;
import org.sopt.domain.user.dto.jwt.UserDetails;
import org.sopt.global.enums.ResponseMessage;
import org.sopt.global.dto.response.SuccessResponse;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/likes/posts/{postId}")
public class PostLikeController {

    private final PostLikeService postLikeService;

    @GetMapping
    public SuccessResponse<Long> getPostLikes(@PathVariable("postId") long postId){
        long counts = postLikeService.countPostLikes(postId);
        return new SuccessResponse<>(ResponseMessage.COUNT_POSTLIKE_SUCCESS.getMessage(), counts);
    }

    @PostMapping
    public SuccessResponse<String> addPostLike(@AuthenticationPrincipal UserDetails userDetails,
                                                               @PathVariable("postId") long postId){
        postLikeService.addPostLike(userDetails.userId(), postId);
        return new SuccessResponse<>(ResponseMessage.CREATE_POSTLIKE_SUCCESS.getMessage());
    }

    @DeleteMapping
    public SuccessResponse<String> deletePostLike(@AuthenticationPrincipal UserDetails userDetails,
                                                                  @PathVariable("postId") long postId){
        postLikeService.removePostLike(userDetails.userId(), postId);
        return new SuccessResponse<>(ResponseMessage.CANCEL_POSTLIKE_SUCCESS.getMessage());
    }

    @GetMapping("/users")
    public SuccessResponse<List<UsersWhoLikedPostDto>> getLikedPostsUserList(@PathVariable("postId") long postId){
        List<UsersWhoLikedPostDto> users = postLikeService.getUsersWhoLikedPost(postId);
        return new SuccessResponse<>(ResponseMessage.GET_POSTLIKE_USERS_SUCCESS.getMessage(), users);
    }
}
