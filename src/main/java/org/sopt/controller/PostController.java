package org.sopt.controller;

import jakarta.validation.Valid;
import org.sopt.domain.Post;
import org.sopt.domain.User;
import org.sopt.dto.request.PostRequestDto;
import org.sopt.dto.response.PostAllResponseDto;
import org.sopt.dto.response.PostDetailResponseDto;
import org.sopt.dto.response.SuccessResponse;
import org.sopt.service.PostService;
import org.sopt.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PostController {

    private final PostService postService;
    private final UserService userService;

    public PostController(PostService postService, UserService userService){
        this.postService = postService;
        this.userService = userService;
    }

    @PostMapping("/posts")
    public ResponseEntity<SuccessResponse<String>> createPost(@RequestHeader("id") Long userId, @RequestBody @Valid final PostRequestDto postRequestDto){
        User user = userService.existsUser(userId);
        String response = postService.createPost(user, postRequestDto);
        return ResponseEntity.ok(new SuccessResponse<>(response));
    }

    @GetMapping("/posts")
    public ResponseEntity<SuccessResponse<List<PostAllResponseDto>>> getAllPosts(){
        List<PostAllResponseDto> allPosts = postService.getAllPosts();
        return ResponseEntity.ok(new SuccessResponse<>(allPosts));
    }

    @GetMapping("/posts/{id}")
    public ResponseEntity<SuccessResponse<PostDetailResponseDto>> getPostById(@PathVariable("id") Long id){
        PostDetailResponseDto postResponseDto = postService.getPostById(id);
        return ResponseEntity.ok(new SuccessResponse<>(postResponseDto));
    }

    @DeleteMapping("/posts/{id}")
    public ResponseEntity<SuccessResponse<String>> deletePostById(@RequestHeader("id") Long userId, @PathVariable("id") Long id){
        User user = userService.existsUser(userId);
        String response = postService.deletePostById(id, user);
        return ResponseEntity.ok(new SuccessResponse<>(response));
    }

    @PatchMapping("/posts/{id}")
    public ResponseEntity<SuccessResponse<String>> updatePostTitle(@RequestHeader("id") Long userId, @PathVariable("id") Long id, @RequestBody @Valid final PostRequestDto postRequestDto){
        User user = userService.existsUser(userId);
        String response = postService.updatePostTitle(id, user, postRequestDto);
        return ResponseEntity.ok(new SuccessResponse<>(response));
    }

    @GetMapping("/posts/search")
    public ResponseEntity<SuccessResponse<List<Post>>> searchPostsByKeyword(@RequestParam("category") String category, @RequestParam("keyword") String keywords){
        List<Post> posts = postService.searchPosts(keywords, category);
        return ResponseEntity.ok(new SuccessResponse<>(posts));
    }
}
