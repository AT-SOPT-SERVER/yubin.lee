package org.sopt.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.sopt.domain.Post;
import org.sopt.domain.User;
import org.sopt.dto.request.CommentRequestDto;
import org.sopt.dto.request.PostRequestDto;
import org.sopt.dto.response.CommentResponse;
import org.sopt.dto.response.PostAllResponseDto;
import org.sopt.dto.response.PostDetailResponseDto;
import org.sopt.dto.response.SuccessResponse;
import org.sopt.service.CommentService;
import org.sopt.service.PostService;
import org.sopt.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final UserService userService;
    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<SuccessResponse<String>> createPost(
            @RequestHeader("userId") Long userId,
            @RequestBody @Valid final PostRequestDto postRequestDto){
        User user = userService.existsUser(userId);
        postService.createPost(user, postRequestDto);
        return ResponseEntity.ok(new SuccessResponse<>(ResponseMessage.CREATE_POST_SUCCESS.getMessage()));
    }

    @GetMapping
    public ResponseEntity<SuccessResponse<List<PostAllResponseDto>>> getAllPosts(){
        List<PostAllResponseDto> allPosts = postService.getAllPosts();
        return ResponseEntity.ok(new SuccessResponse<>(allPosts));
    }

    @GetMapping("/{postId}")
    public ResponseEntity<SuccessResponse<PostDetailResponseDto>> getPostById(@PathVariable("postId") Long id){
        PostDetailResponseDto postResponseDto = postService.getPostById(id);
        return ResponseEntity.ok(new SuccessResponse<>(postResponseDto));
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<SuccessResponse<String>> deletePostById(
            @RequestHeader("userId") Long userId,
            @PathVariable("postId") Long id){
        User user = userService.existsUser(userId);
        postService.deletePostById(id, user);
        return ResponseEntity.ok(new SuccessResponse<>(ResponseMessage.DELETE_POST_SUCCESS.getMessage()));
    }

    @PatchMapping("/{postId}")
    public ResponseEntity<SuccessResponse<String>> updatePostTitle(
            @RequestHeader("userId") Long userId,
            @PathVariable("postId") Long id,
            @RequestBody @Valid final PostRequestDto postRequestDto){
        User user = userService.existsUser(userId);
        postService.updatePosts(id, user, postRequestDto);
        return ResponseEntity.ok(new SuccessResponse<>(ResponseMessage.UPDATE_POST_SUCCESS.getMessage()));
    }

    @GetMapping("/search")
    public ResponseEntity<SuccessResponse<List<Post>>> searchPostsByKeyword(
            @RequestParam("category") String category,
            @RequestParam("keyword") String keywords){
        List<Post> posts = postService.searchPosts(keywords, category);
        return ResponseEntity.ok(new SuccessResponse<>(posts));
    }

    // 댓글 기능
    @PostMapping("/{postId}/comments")
    public ResponseEntity<SuccessResponse<String>> addComment(@RequestHeader("userId") Long userId,
                                                              @PathVariable("postId") Long postId,
                                                              @RequestBody @Valid final CommentRequestDto commentRequestDto){
        commentService.createComment(userId, postId, commentRequestDto);
        return ResponseEntity.ok(new SuccessResponse<>(ResponseMessage.CREATE_COMMENT_SUCCESS.getMessage()));
    }

    @PatchMapping("/{postId}/comments/{commentId}")
    public ResponseEntity<SuccessResponse<String>> updateComment(@RequestHeader("userId") long userId,
                                                                 @PathVariable("postId") long postId,
                                                                 @PathVariable("commentId") long commentId,
                                                                 @RequestBody @Valid final CommentRequestDto commentRequestDto){
        commentService.updateComment(userId, postId, commentId, commentRequestDto);
        return ResponseEntity.ok(new SuccessResponse<>(ResponseMessage.UPDATE_COMMENT_SUCCESS.getMessage()));
    }

    @DeleteMapping("/{postId}/comments/{commentId}")
    public ResponseEntity<SuccessResponse<String>> deleteComment(@RequestHeader("userId") Long userId,
                                                                 @PathVariable("postId") Long postId,
                                                                 @PathVariable("commentId") Long commentId){
        commentService.deleteComment(userId, postId, commentId);
        return ResponseEntity.ok(new SuccessResponse<>(ResponseMessage.DELETE_COMMENT_SUCCESS.getMessage()));
    }

    @GetMapping("/{postId}/comments")
    public ResponseEntity<SuccessResponse<List<CommentResponse>>> getComments(@PathVariable("postId") Long postId){
        List<CommentResponse> response = commentService.getComments(postId);
        return ResponseEntity.ok(new SuccessResponse<>(response));
    }
}
