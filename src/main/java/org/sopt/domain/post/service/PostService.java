package org.sopt.domain.post.service;

import lombok.RequiredArgsConstructor;
import org.sopt.domain.post.domain.Post;
import org.sopt.domain.post.dto.response.PostAllResponseDto;
import org.sopt.domain.user.domain.User;
import org.sopt.domain.post.dto.request.PostRequestDto;
import org.sopt.domain.post.dto.response.PostListsDto;
import org.sopt.domain.post.dto.response.PostDetailResponseDto;
import org.sopt.domain.user.repository.UserRepository;
import org.sopt.global.exception.CustomAccessDeniedException;
import org.sopt.global.exception.CustomBadRequestException;
import org.sopt.global.exception.CustomNotFoundException;
import org.sopt.global.enums.ErrorCode;
import org.sopt.domain.post.repository.PostRepository;
import org.sopt.global.exception.UnauthenticatedException;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    // 게시글 저장
    public void createPost(long userId, PostRequestDto postRequestDto) throws IllegalArgumentException{
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UnauthenticatedException(ErrorCode.NOT_FOUND_USER));

        canCreatePost(LocalDateTime.now(), user);
        duplicatePost(postRequestDto.title(), null);

        Post post = postRequestDto.from(user);

        postRepository.save(post);
    }

    // 전체 게시글 조회 (최신순)
    @Transactional(readOnly = true)
    @Cacheable(
            key = "'page=' + #pageNumber + ',size=' + #pageSize",
            value = "findAllPosts"
    )
    public PostAllResponseDto getAllPosts(int pageNumber, int pageSize){
        if (pageNumber < 0) throw new CustomBadRequestException(ErrorCode.NOT_FOUND_PAGE);
        Sort sort = Sort.by(Sort.Direction.DESC, "createdDate");
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<PostListsDto> postListsDto = postRepository.findAll(pageable).map(PostListsDto::from);
        return PostAllResponseDto.of(postListsDto);
    }

    // 게시글 상세 조회
    @Cacheable(
            value = "findPostDetail",
            key = "#id"
    )
    public PostDetailResponseDto getPostById(Long id){
        Post post = postRepository.findById(id).orElseThrow(()-> new CustomNotFoundException(ErrorCode.NOT_FOUND_POST));
        return PostDetailResponseDto.from(post);
    }

    // 게시글 삭제
    @Caching(evict = {
            @CacheEvict(value = "findPostDetail", key = "#id"),
            @CacheEvict(value = "findAllPosts", allEntries = true)
    })
    public void deletePostById(Long id, long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new CustomNotFoundException(ErrorCode.NOT_FOUND_USER));
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new CustomNotFoundException(ErrorCode.NOT_FOUND_POST));

        if (!post.getUser().getId().equals(user.getId())) {
            throw new CustomAccessDeniedException(ErrorCode.POST_ACCESS_DENIED);
        }
        postRepository.delete(post);
    }

    // 게시글 수정
    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "findPostDetail", key = "#id"),
            @CacheEvict(value = "findAllPosts", allEntries = true)
    })
    public void updatePosts(Long id, long userId, PostRequestDto postRequestDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomNotFoundException(ErrorCode.NOT_FOUND_USER));
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new CustomNotFoundException(ErrorCode.NOT_FOUND_POST));

        if (!post.getUser().getId().equals(user.getId())) {
            throw new CustomAccessDeniedException(ErrorCode.POST_ACCESS_DENIED);
        }
        duplicatePost(postRequestDto.title(), id);
        post.updateTitle(postRequestDto.title());
        post.updateContent(postRequestDto.content());
    }

    // 카테고리별 게시물 검색
    public Page<PostListsDto> searchPosts(String keyword, int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        return postRepository.searchByWhere(keyword, pageable);
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
