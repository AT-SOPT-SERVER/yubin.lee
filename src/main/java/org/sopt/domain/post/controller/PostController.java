package org.sopt.domain.post.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.sopt.domain.like.dto.response.UsersWhoLikedPostDto;
import org.sopt.domain.post.service.PostLikeService;
import org.sopt.global.ResponseMessage;
import org.sopt.domain.post.model.Post;
import org.sopt.domain.user.model.User;
import org.sopt.domain.post.dto.request.CommentRequestDto;
import org.sopt.domain.post.dto.request.PostRequestDto;
import org.sopt.domain.post.dto.response.CommentResponse;
import org.sopt.domain.post.dto.response.PostAllResponseDto;
import org.sopt.domain.post.dto.response.PostDetailResponseDto;
import org.sopt.global.dto.SuccessResponse;
import org.sopt.domain.post.service.CommentService;
import org.sopt.domain.post.service.PostService;
import org.sopt.domain.user.service.UserService;
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
    private final PostLikeService postLikeService;

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
    public ResponseEntity<SuccessResponse<String>> deleteComment(@RequestHeader("userId") long userId,
                                                                 @PathVariable("postId") long postId,
                                                                 @PathVariable("commentId") long commentId){
        commentService.deleteComment(userId, postId, commentId);
        return ResponseEntity.ok(new SuccessResponse<>(ResponseMessage.DELETE_COMMENT_SUCCESS.getMessage()));
    }

    @GetMapping("/{postId}/comments")
    public ResponseEntity<SuccessResponse<List<CommentResponse>>> getComments(@PathVariable("postId") long postId){
        List<CommentResponse> response = commentService.getComments(postId);
        return ResponseEntity.ok(new SuccessResponse<>(response));
    }

    // 좋아요 기능
    // 게시물 좋아요 개수 조회
    @GetMapping("/{postId}/likes")
    public ResponseEntity<SuccessResponse<Long>> getPostLikes(@RequestHeader("userId") long userId,
                                                            @PathVariable("postId") long postId){
        long counts = postLikeService.countPostLikes(postId);
        return ResponseEntity.ok(new SuccessResponse<>(counts));
    }

    // 게시물 좋아요 등록
    @PostMapping("/{postId}/likes")
    public ResponseEntity<SuccessResponse<String>> addPostLike(@RequestHeader("userId") long userId,
                                                                @PathVariable("postId") long postId){
        postLikeService.addPostLike(userId, postId);
        return ResponseEntity.ok(new SuccessResponse<>(ResponseMessage.CREATE_POSTLIKE_SUCCESS.getMessage()));
    }

    // 게시물 좋아요 취소
    @DeleteMapping("/{postId}/likes")
    public ResponseEntity<SuccessResponse<String>> deletePostLike(@RequestHeader("userId") long userId,
                                                                  @PathVariable("postId") long postId){
        postLikeService.removePostLike(userId, postId);
        return ResponseEntity.ok(new SuccessResponse<>(ResponseMessage.CANCEL_POSTLIKE_SUCCESS.getMessage()));
    }

    // 게시물 좋아요 누른 유저 목록 반환
    @GetMapping("/{postId}/likes/users")
    public ResponseEntity<SuccessResponse<List<UsersWhoLikedPostDto>>> getLikedPostsUserList(@RequestHeader("userId") long userId,
                                                                                             @PathVariable("postId") long postId){
        List<UsersWhoLikedPostDto> users = postLikeService.getUsersWhoLikedPost(postId);
        return ResponseEntity.ok(new SuccessResponse<>(users));
    }


    // 댓글 좋아요 개수 조회
    @GetMapping("/{postId}/comments/{commentId}/likes")
    public ResponseEntity<SuccessResponse<String>> getPostCommentLikes(@RequestHeader("userId") long userId,
                                                                         @PathVariable("postId") long postId,
                                                                         @PathVariable("commentId") long commentId){
        return ResponseEntity.ok(new SuccessResponse<>(""));
    }

    // 댓글 좋아요 등록
    @PostMapping("/{postId}/comments/{commentId}/likes")
    public ResponseEntity<SuccessResponse<String>> addPostCommentLike(@RequestHeader("userId") long userId,
                                                                      @PathVariable("postId") long postId,
                                                                      @PathVariable("commentId") long commentId){
        return ResponseEntity.ok(new SuccessResponse<>(""));
    }

    // 댓글 좋아요 취소
    @DeleteMapping("/{postId}/comments/{commentId}/likes")
    public ResponseEntity<SuccessResponse<String>> deletePostCommentLike(@RequestHeader("userId") long userId,
                                                                         @PathVariable("postId") long postId,
                                                                         @PathVariable("commentId") long commentId){
        return ResponseEntity.ok(new SuccessResponse<>(""));
    }
}
