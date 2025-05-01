package org.sopt.controller;

import jakarta.validation.Valid;
import org.sopt.domain.Post;
import org.sopt.domain.User;
import org.sopt.dto.request.PostRequestDto;
import org.sopt.dto.response.PostResponseDto;
import org.sopt.dto.response.SuccessResponse;
import org.sopt.service.PostService;
import org.sopt.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
public class PostController {

    private final PostService postService;
    private final UserService userService;

    public PostController(PostService postService, UserService userService){
        this.postService = postService;
        this.userService = userService;
    }

    @PostMapping("/posts")
    public ResponseEntity<SuccessResponse> createPost(@RequestHeader Long userId, @RequestBody @Valid final PostRequestDto postRequestDto){
        User user = userService.existsUser(userId);
        SuccessResponse successResponse = postService.createPost(user, postRequestDto);
        return ResponseEntity.ok(successResponse);
    }

    @GetMapping("/posts")
    public ResponseEntity<List<Post>> getAllPosts(){
        List<Post> allPosts = postService.getAllPosts();
        return ResponseEntity.ok(allPosts);
    }

    @GetMapping("/posts/{id}")
    public ResponseEntity<PostResponseDto> getPostById(@PathVariable("id") Long id){
        PostResponseDto postResponseDto = postService.getPostById(id);
        return ResponseEntity.ok(postResponseDto);
    }

    @DeleteMapping("/posts/{id}")
    public ResponseEntity<SuccessResponse> deletePostById(@RequestHeader Long userId, @PathVariable("id") Long id){
        User user = userService.existsUser(userId);
        SuccessResponse successResponse = postService.deletePostById(id, user);
        return ResponseEntity.ok(successResponse);
    }

    @PatchMapping("/posts/{id}")
    public ResponseEntity<SuccessResponse> updatePostTitle(@RequestHeader Long userId, @PathVariable("id") Long id, @RequestBody @Valid final PostRequestDto postRequestDto){
        User user = userService.existsUser(userId);
        SuccessResponse successResponse = postService.updatePostTitle(id, user, postRequestDto);
        return ResponseEntity.ok(successResponse);
    }

    @GetMapping("/posts/search")
    public ResponseEntity<List<Post>> searchPostsByKeyword(@RequestParam("keywords") String keywords){
        List<Post> posts = postService.searchPostsByKeyword(keywords);
        return ResponseEntity.ok(posts);
    }
}
