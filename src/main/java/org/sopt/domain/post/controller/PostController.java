package org.sopt.domain.post.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.sopt.domain.post.dto.response.PostAllResponseDto;
import org.sopt.global.ResponseMessage;
import org.sopt.domain.user.model.User;
import org.sopt.domain.post.dto.request.PostRequestDto;
import org.sopt.domain.post.dto.response.PostListsDto;
import org.sopt.domain.post.dto.response.PostDetailResponseDto;
import org.sopt.global.dto.response.SuccessResponse;
import org.sopt.domain.post.service.PostService;
import org.sopt.domain.user.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final UserService userService;

    @GetMapping
    public SuccessResponse<PostAllResponseDto> getAllPosts(@RequestParam("pageSize") int pageSize,
                                                           @RequestParam("pageNumber") int pageNumber) {
        Page<PostListsDto> allPosts = postService.getAllPosts(pageNumber, pageSize);
        return new SuccessResponse<>(PostAllResponseDto.of(allPosts));
    }

    @PostMapping
    public SuccessResponse<String> createPost(
            @RequestHeader("userId") Long userId,
            @RequestBody @Valid final PostRequestDto postRequestDto){

        User user = userService.existsUser(userId);
        postService.createPost(user, postRequestDto);
        return new SuccessResponse<>(ResponseMessage.CREATE_POST_SUCCESS.getMessage());
    }

    @GetMapping("/{postId}")
    public SuccessResponse<PostDetailResponseDto> getPostById(@PathVariable("postId") Long id){

        PostDetailResponseDto postResponseDto = postService.getPostById(id);
        return new SuccessResponse<>(postResponseDto);
    }

    @PatchMapping("/{postId}")
    public SuccessResponse<String> updatePostTitle(
            @RequestHeader("userId") Long userId,
            @PathVariable("postId") Long id,
            @RequestBody @Valid final PostRequestDto postRequestDto){

        User user = userService.existsUser(userId);
        postService.updatePosts(id, user, postRequestDto);
        return new SuccessResponse<>(ResponseMessage.UPDATE_POST_SUCCESS.getMessage());
    }

    @DeleteMapping("/{postId}")
    public SuccessResponse<String> deletePostById(
            @RequestHeader("userId") Long userId,
            @PathVariable("postId") Long id){

        User user = userService.existsUser(userId);
        postService.deletePostById(id, user);
        return new SuccessResponse<>(ResponseMessage.DELETE_POST_SUCCESS.getMessage());
    }

    @GetMapping("/search")
    public SuccessResponse<PostAllResponseDto> searchPostsByKeyword(
            @RequestParam("category") String category,
            @RequestParam("keyword") String keywords,
            @RequestParam("pageSize") int pageSize,
            @RequestParam("pageNumber") int pageNumber){

        Page<PostListsDto> posts = postService.searchPosts(keywords, category, pageNumber, pageSize);
        return new SuccessResponse<>(PostAllResponseDto.of(posts));
    }
}
