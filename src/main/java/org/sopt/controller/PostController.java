package org.sopt.controller;

import org.sopt.domain.Post;
import org.sopt.dto.request.PostRequestDto;
import org.sopt.dto.request.SearchRequestDto;
import org.sopt.dto.response.PostResponseDto;
import org.sopt.service.PostService;
import org.sopt.util.Validation;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<String> createPost(@RequestBody final PostRequestDto postRequestDto){
        try {
            Validation.isTitleValid(postRequestDto.getTitle());
            postService.createPost(postRequestDto.getTitle());
            return ResponseEntity.ok("게시물이 저장되었습니다.");
        } catch (IllegalArgumentException e){ // 클라이언트의 입력이 잘못됨
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/posts")
    public ResponseEntity<List<Post>> getAllPosts(){
        return ResponseEntity.ok(postService.getAllPosts());
    }

    @GetMapping("/posts/{id}")
    public ResponseEntity<?> getPostById(@PathVariable("id") Long id){
        try {
            PostResponseDto postResponseDto = postService.getPostById(id);
            return ResponseEntity.ok(postResponseDto);
        } catch (NoSuchElementException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @DeleteMapping("/posts/{id}")
    public ResponseEntity<String> deletePostById(@PathVariable("id") Long id){
        try{
            postService.deletePostById(id);
            return ResponseEntity.ok("게시물 삭제가 완료되었습니다.");
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PutMapping("/posts/{id}")
    public ResponseEntity<String> updatePostTitle(@PathVariable("id") Long id, @RequestBody final PostRequestDto postRequestDto){
        try {
            Validation.isTitleValid(postRequestDto.getTitle());
            postService.updatePostTitle(id, postRequestDto.getTitle());
            return ResponseEntity.ok("게시물 수정이 완료되었습니다.");
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/posts/search")
    public List<Post> searchPostsByKeyword(@RequestBody SearchRequestDto searchRequestDto){
        return postService.searchPostsByKeyword(searchRequestDto.getKeyword());
    }
}
