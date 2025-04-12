package org.sopt.service;

import org.sopt.domain.Post;
import org.sopt.repository.PostRepository;
import org.sopt.util.GeneratorId;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class PostService {

    // postRepository 가져오기
    private final PostRepository postRepository = new PostRepository();

    // 게시글 저장
    public void createPost(String title) throws IllegalArgumentException{
        canCreatePost(LocalDateTime.now());
        duplicatePost(title);
        Post post = new Post(GeneratorId.generateId(), title);
        postRepository.save(post);
    }

    // 전체 게시글 조회
    public List<Post> getAllPosts(){
        return postRepository.findAll();
    }

    // 게시글 상세 조회
    public Post getPostById(int id){
        return postRepository.findPostById(id);
    }

    // 게시글 삭제
    public boolean deletePostById(int id){
        return postRepository.deletePostById(id);
    }

    // 게시글 수정
    public boolean updatePostTitle(int id, String title) throws IllegalArgumentException{
        duplicatePost(title);
        Post post = postRepository.findPostById(id);
        if (post!= null) {
            post.setTitle(title);
            return true;
        }
        return false;
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
