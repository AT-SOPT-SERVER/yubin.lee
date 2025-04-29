package org.sopt.controller;

import jakarta.validation.Valid;
import org.sopt.domain.Post;
import org.sopt.dto.request.PostRequestDto;
import org.sopt.dto.response.PostResponseDto;
import org.sopt.dto.response.SuccessResponse;
import org.sopt.service.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PostController {

    private final PostService postService;

    public PostController(PostService postService){
        this.postService = postService;
    }

    @PostMapping("/posts")
    public ResponseEntity<SuccessResponse> createPost(@RequestHeader Long userId, @RequestBody @Valid final PostRequestDto postRequestDto){
        SuccessResponse successResponse = postService.createPost(userId, postRequestDto);
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
    public ResponseEntity<SuccessResponse> deletePostById(@PathVariable("id") Long id){
        SuccessResponse successResponse = postService.deletePostById(id);
        return ResponseEntity.ok(successResponse);
    }

    @PatchMapping("/posts/{id}")
    public ResponseEntity<SuccessResponse> updatePostTitle(@RequestHeader Long userId, @PathVariable("id") Long id, @RequestBody @Valid final PostRequestDto postRequestDto){
        SuccessResponse successResponse = postService.updatePostTitle(id, userId, postRequestDto);
        return ResponseEntity.ok(successResponse);
    }

    @GetMapping("/posts/search")
    public ResponseEntity<List<Post>> searchPostsByKeyword(@RequestParam("keywords") String keywords){
        List<Post> posts = postService.searchPostsByKeyword(keywords);
        return ResponseEntity.ok(posts);
    }
}
