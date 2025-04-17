package org.sopt.controller;

import org.sopt.domain.Post;
import org.sopt.dto.request.PostRequestDto;
import org.sopt.dto.request.SearchRequestDto;
import org.sopt.service.PostService;
import org.sopt.util.Validation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
public class PostController {

    private final PostService postService;

    public PostController(PostService postService){
        this.postService = postService;
    }

    @PostMapping("/posts")
    public ResponseEntity<?> createPost(@RequestBody final PostRequestDto postRequestDto){
        try {
            Validation.isTitleValid(postRequestDto.getTitle());
            postService.createPost(postRequestDto.getTitle());
            return ResponseEntity.ok("게시물이 저장되었습니다.");
        } catch (IllegalArgumentException e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping("/posts")
    public ResponseEntity<?> getAllPosts(){
        return ResponseEntity.ok(postService.getAllPosts());
    }

    @GetMapping("/posts/{id}")
    public ResponseEntity<?> getPostById(@PathVariable("id") Long id){
        try {
            postService.getPostById(id);
            return ResponseEntity.ok("");
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @DeleteMapping("/posts/{id}")
    public ResponseEntity<?> deletePostById(@PathVariable("id") Long id){
        try{
            postService.deletePostById(id);
            return ResponseEntity.ok("게시물 삭제가 완료되었습니다.");
        } catch (NoSuchElementException | IllegalArgumentException e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @PutMapping("/posts/{id}")
    public ResponseEntity<?> updatePostTitle(@PathVariable("id") Long id, @RequestBody final PostRequestDto postRequestDto){
        try {
            Validation.isTitleValid(postRequestDto.getTitle());
            postService.updatePostTitle(id, postRequestDto.getTitle());
            return ResponseEntity.ok("게시물 수정이 완료되었습니다.");
        } catch (NoSuchElementException | IllegalArgumentException e){
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @PostMapping("/posts/search")
    public List<Post> searchPostsByKeyword(@RequestBody SearchRequestDto searchRequestDto){
        return postService.searchPostsByKeyword(searchRequestDto.getKeyword());
    }
}
