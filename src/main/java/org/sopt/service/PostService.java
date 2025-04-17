package org.sopt.service;

import org.sopt.domain.Post;
import org.sopt.repository.PostRepository;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

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
    public Post getPostById(Long id){
        Optional<Post> postOptional = postRepository.findById(id);
        if (postOptional.isPresent()){
            return postOptional.get();
        } else {
            throw new NoSuchElementException("게시물이 존재하지 않습니다.");
        }
    }

    // 게시글 삭제
    public void deletePostById(Long id){
        postRepository.deleteById(id);
    }

    // 게시글 수정
    public void updatePostTitle(Long id, String title) throws IllegalArgumentException{
        duplicatePost(title);
        Optional<Post> postOptional = postRepository.findById(id);
        if (postOptional.isPresent()) {
            postOptional.get().setTitle(title);
        } else {
            throw new NoSuchElementException("게시물이 존재하지 않습니다.");
        }

    }

    // 게시글 찾기
    public List<Post> searchPostsByKeyword(String keyword) {
        List<Post> posts = postRepository.findAll();
        return posts.stream().filter(post -> post.getTitle().toLowerCase().contains(keyword.toLowerCase())).toList();
    }

    // 중복된 게시물
    private void duplicatePost(String title){
        boolean isDuplicate = postRepository.findAll().stream().anyMatch(post -> post.getTitle().equalsIgnoreCase(title));
        if (isDuplicate){
            throw new IllegalArgumentException("❌ 중복된 게시물입니다.");
        }
    }

    // 게시물 작성 3분으로 제한
    private void canCreatePost(LocalDateTime now) {
        List<Post> posts = postRepository.findAll();

        if (!posts.isEmpty()) {
            Post lastPost = posts.get(posts.size() - 1);
            if (Duration.between(lastPost.getTime(), now).toMinutes() < 3) {
                throw new IllegalArgumentException("게시글 작성은 3분 뒤에 가능합니다.");
            }
        }
    }
}
