package org.sopt.service;

import org.sopt.domain.Post;
import org.sopt.domain.User;
import org.sopt.dto.request.PostRequestDto;
import org.sopt.dto.response.PostAllResponseDto;
import org.sopt.dto.response.PostDetailResponseDto;
import org.sopt.exception.CustomBadRequestException;
import org.sopt.exception.CustomNotFoundException;
import org.sopt.exception.ErrorCode;
import org.sopt.repository.PostRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserService userService;

    public PostService(PostRepository postRepository, UserService userService){
        this.postRepository = postRepository;
        this.userService = userService;
    }

    // 게시글 저장
    public String createPost(User user, PostRequestDto postRequestDto) throws IllegalArgumentException{
        // 포스트 검증
        canCreatePost(LocalDateTime.now(), user);
        duplicatePost(postRequestDto.title(), null);
        // dto -> Entity 변환
        Post post = postRequestDto.toEntity(user);
        // 저장
        postRepository.save(post);
        return "게시물이 저장되었습니다.";
    }

    // 전체 게시글 조회 (최신순)
    @Transactional(readOnly = true)
    public List<PostAllResponseDto> getAllPosts(){
        Sort sort = Sort.by(Sort.Direction.DESC, "createdDate");
        return postRepository.findAll(sort).stream().map(PostAllResponseDto::from).toList();
    }

    // 게시글 상세 조회
    public PostDetailResponseDto getPostById(Long id){
        Post post = postRepository.findById(id).orElseThrow(()-> new CustomNotFoundException(ErrorCode.NOT_FOUND));
        return PostDetailResponseDto.from(post);
    }

    // 게시글 삭제
    public String deletePostById(Long id, User user) {
        Post post = postRepository.findById(id).orElseThrow(() -> new CustomNotFoundException(ErrorCode.NOT_FOUND));
        userService.validatePostOwnership(post, user);
        postRepository.delete(post);
        return "게시물이 삭제되었습니다.";
    }

    // 게시글 수정
    @Transactional
    public String updatePosts(Long id, User user, PostRequestDto postRequestDto) {
        Post post = postRepository.findById(id).orElseThrow(() -> new CustomNotFoundException(ErrorCode.NOT_FOUND));
        userService.validatePostOwnership(post, user);
        duplicatePost(postRequestDto.title(), id);
        post.updateTitle(postRequestDto.title());
        post.updateContent(postRequestDto.content());
        return "게시물 수정이 완료되었습니다.";
    }

    // 카테고리별 게시물 검색
    public List<Post> searchPosts(String keyword, String category) {
        return switch (category.toLowerCase()) {
            case "title" -> postRepository.findByTitleContainingIgnoreCase(keyword);
            case "author" -> postRepository.findByUserNameContainingIgnoreCase(keyword);
            default -> throw new CustomBadRequestException(ErrorCode.INVALID_INPUT_VALUE);
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
