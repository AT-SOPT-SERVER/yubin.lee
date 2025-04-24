package org.sopt.controller;

import jakarta.validation.Valid;
import org.sopt.domain.Post;
import org.sopt.dto.request.PostRequestDto;
import org.sopt.dto.response.PostResponseDto;
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
    public ResponseEntity<String> createPost(@RequestBody @Valid final PostRequestDto postRequestDto){
        postService.createPost(postRequestDto.title());
        return ResponseEntity.ok("게시물이 저장되었습니다.");
    }

    @GetMapping("/posts")
    public ResponseEntity<List<Post>> getAllPosts(){
        return ResponseEntity.ok(postService.getAllPosts());
    }

    @GetMapping("/posts/{id}")
    public ResponseEntity<PostResponseDto> getPostById(@PathVariable("id") Long id){
        PostResponseDto postResponseDto = postService.getPostById(id);
        return ResponseEntity.ok(postResponseDto);
    }

    @DeleteMapping("/posts/{id}")
    public ResponseEntity<String> deletePostById(@PathVariable("id") Long id){
        postService.deletePostById(id);
        return ResponseEntity.ok("게시물 삭제가 완료되었습니다.");
    }

    @PutMapping("/posts/{id}")
    public ResponseEntity<String> updatePostTitle(@PathVariable("id") Long id, @RequestBody @Valid final PostRequestDto postRequestDto){
        postService.updatePostTitle(id, postRequestDto.title());
        return ResponseEntity.ok("게시물 수정이 완료되었습니다.");
    }

    @PostMapping("/search")
    public ResponseEntity<List<Post>> searchPostsByKeyword(@RequestParam("keywords") String keywords){
        List<Post> posts = postService.searchPostsByKeyword(keywords);
        return ResponseEntity.ok(posts);
    }
}
