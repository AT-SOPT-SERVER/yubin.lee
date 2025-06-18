package org.sopt.domain.post.service;

import lombok.RequiredArgsConstructor;
import org.sopt.domain.post.model.Post;
import org.sopt.domain.user.model.User;
import org.sopt.domain.user.service.UserService;
import org.sopt.domain.post.dto.request.PostRequestDto;
import org.sopt.domain.post.dto.response.PostListsDto;
import org.sopt.domain.post.dto.response.PostDetailResponseDto;
import org.sopt.global.exception.CustomBadRequestException;
import org.sopt.global.exception.CustomNotFoundException;
import org.sopt.global.ErrorCode;
import org.sopt.domain.post.repository.PostRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserService userService;

    // 게시글 저장
    public void createPost(User user, PostRequestDto postRequestDto) throws IllegalArgumentException{
        // 포스트 검증
        canCreatePost(LocalDateTime.now(), user);
        duplicatePost(postRequestDto.title(), null);
        // dto -> Entity 변환
        Post post = postRequestDto.from(user);
        // 저장
        postRepository.save(post);
    }

    // 전체 게시글 조회 (최신순)
    @Transactional(readOnly = true)
    public Page<PostListsDto> getAllPosts(int pageNumber, int pageSize){
        Sort sort = Sort.by(Sort.Direction.DESC, "createdDate");
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        return postRepository.findAll(pageable).map(PostListsDto::from);
    }

    // 게시글 상세 조회
    public PostDetailResponseDto getPostById(Long id){
        Post post = postRepository.findById(id).orElseThrow(()-> new CustomNotFoundException(ErrorCode.NOT_FOUND_POST));
        return PostDetailResponseDto.from(post);
    }

    // 게시글 삭제
    public void deletePostById(Long id, User user) {
        Post post = postRepository.findById(id).orElseThrow(() -> new CustomNotFoundException(ErrorCode.NOT_FOUND_POST));
        userService.validatePostOwnership(post, user);
        postRepository.delete(post);
    }

    // 게시글 수정
    @Transactional
    public void updatePosts(Long id, User user, PostRequestDto postRequestDto) {
        Post post = postRepository.findById(id).orElseThrow(() -> new CustomNotFoundException(ErrorCode.NOT_FOUND_POST));
        userService.validatePostOwnership(post, user);
        duplicatePost(postRequestDto.title(), id);
        post.updateTitle(postRequestDto.title());
        post.updateContent(postRequestDto.content());
    }

    // 카테고리별 게시물 검색
    public Page<PostListsDto> searchPosts(String keyword, String category, int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return switch (category.toLowerCase()) {
            case "title" ->
                    postRepository.findByTitleContainingIgnoreCase(keyword, pageable)
                    .map(PostListsDto::from);
            case "author" ->
                    postRepository.findByUserNameContainingIgnoreCase(keyword, pageable)
                    .map(PostListsDto::from);
            default ->
                    throw new CustomBadRequestException(ErrorCode.INVALID_INPUT_VALUE);
        };
    }

    // 중복된 게시물
    private void duplicatePost(String title, Long id) {
        boolean isDuplicate = (id == null)
                ? postRepository.existsByTitle(title) // 생성 시
                : postRepository.existsByTitleAndIdNot(title, id); // 수정 시

        if (isDuplicate) {
            throw new CustomBadRequestException(ErrorCode.POST_DUPLICATED);
        }
    }


    // 게시물 작성 3분으로 제한
    private void canCreatePost(LocalDateTime now, User user) {
        postRepository.findTopByUserOrderByCreatedDateDesc(user)
                .ifPresent(lastPost -> {
                    if (Duration.between(lastPost.getCreatedDate(), now).toMinutes() < 3) {
                        throw new CustomBadRequestException(ErrorCode.POST_CREATION_LIMIT);
                    }
                });
    }

}
