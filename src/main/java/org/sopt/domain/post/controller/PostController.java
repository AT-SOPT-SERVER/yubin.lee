package org.sopt.domain.post.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.sopt.domain.post.dto.response.PostAllResponseDto;
import org.sopt.domain.user.dto.jwt.UserDetails;
import org.sopt.global.enums.ResponseMessage;
import org.sopt.domain.post.dto.request.PostRequestDto;
import org.sopt.domain.post.dto.response.PostListsDto;
import org.sopt.domain.post.dto.response.PostDetailResponseDto;
import org.sopt.global.dto.response.SuccessResponse;
import org.sopt.domain.post.service.PostService;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping
    public SuccessResponse<PostAllResponseDto> getAllPosts(@RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                                           @RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber) {
        PostAllResponseDto allPosts = postService.getAllPosts(pageNumber, pageSize);
        return new SuccessResponse<>(ResponseMessage.GET_POST_SUCCESS.getMessage(), allPosts);
    }

    @PostMapping
    public SuccessResponse<String> createPost(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody @Valid final PostRequestDto postRequestDto){

        postService.createPost(userDetails.userId(), postRequestDto);
        return new SuccessResponse<>(ResponseMessage.CREATE_POST_SUCCESS.getMessage());
    }

    @GetMapping("/{postId}")
    public SuccessResponse<PostDetailResponseDto> getPostById(@PathVariable("postId") Long id){

        PostDetailResponseDto postResponseDto = postService.getPostById(id);
        return new SuccessResponse<>(ResponseMessage.GET_POST_DETAIL_SUCCESS.getMessage(), postResponseDto);
    }

    @PatchMapping("/{postId}")
    public SuccessResponse<String> updatePostTitle(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable("postId") Long id,
            @RequestBody @Valid final PostRequestDto postRequestDto){

        postService.updatePosts(id, userDetails.userId(), postRequestDto);
        return new SuccessResponse<>(ResponseMessage.UPDATE_POST_SUCCESS.getMessage());
    }

    @DeleteMapping("/{postId}")
    public SuccessResponse<String> deletePostById(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable("postId") Long id){

        postService.deletePostById(id, userDetails.userId());
        return new SuccessResponse<>(ResponseMessage.DELETE_POST_SUCCESS.getMessage());
    }

    @GetMapping("/search")
    public SuccessResponse<PostAllResponseDto> searchPostsByKeyword(
            @RequestParam("category") String category,
            @RequestParam("keyword") String keywords,
            @RequestParam("pageSize") int pageSize,
            @RequestParam("pageNumber") int pageNumber){

        Page<PostListsDto> posts = postService.searchPosts(keywords, category, pageNumber, pageSize);
        return new SuccessResponse<>(ResponseMessage.SEARCH_POST_SUCCESS.getMessage(), PostAllResponseDto.of(posts));
    }
}
