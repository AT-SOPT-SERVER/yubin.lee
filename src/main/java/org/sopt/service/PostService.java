package org.sopt.service;

import org.sopt.domain.Post;
import org.sopt.dto.response.PostResponseDto;
import org.sopt.repository.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class PostService {

    // postRepository 가져오기
    private final PostRepository postRepository;

    public PostService(PostRepository postRepository){
        this.postRepository = postRepository;
    }

    // 게시글 저장
    public void createPost(String title) throws IllegalArgumentException{
        canCreatePost(LocalDateTime.now());
        duplicatePost(title);
        Post post = new Post(title);
        postRepository.save(post);
    }

    // 전체 게시글 조회
    public List<Post> getAllPosts(){
        return postRepository.findAll();
    }

    // 게시글 상세 조회
    public PostResponseDto getPostById(Long id){
        Post post = postRepository.findById(id).orElseThrow(()-> new NoSuchElementException("게시물이 존재하지 않습니다."));
        return new PostResponseDto(post);
    }

    // 게시글 삭제
    public void deletePostById(Long id){
        postRepository.deleteById(id);
    }

    // 게시글 수정
    @Transactional // 해당 어노테이션을 붙이지 않으면 자동으로 수정이 안됨
    public void updatePostTitle(Long id, String title) {
        duplicatePost(title);
        Post post = postRepository.findById(id).orElseThrow(() -> new NoSuchElementException("게시물이 존재하지 않습니다."));
        post.setTitle(title);
    }

    // 게시글 찾기
    public List<Post> searchPostsByKeyword(String keyword) {
        return postRepository.findByTitleContainingIgnoreCase(keyword);
    }

    // 중복된 게시물
    private void duplicatePost(String title){
        if (postRepository.existsByTitle(title)){
            throw new IllegalArgumentException("게시물이 이미 존재합니다.");
        }
    }

    // 게시물 작성 3분으로 제한
    private void canCreatePost(LocalDateTime now) {
        postRepository.findTopByOrderByTimeDesc()
                .ifPresent(lastPost -> {
                    if (Duration.between(lastPost.getTime(), now).toMinutes() < 3) {
                        throw new IllegalArgumentException("게시물 작성은 3분 뒤에 가능합니다.");
                    }
                });
    }
}
