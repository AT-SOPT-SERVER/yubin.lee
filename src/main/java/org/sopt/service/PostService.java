package org.sopt.service;

import org.sopt.domain.Post;
import org.sopt.repository.PostRepository;

import java.util.List;

public class PostService {

    // postRepository 가져오기
    private final PostRepository postRepository = new PostRepository();
    private int postId = 1;

    // 게시글 저장
    public void createPost(String title){
        Post post = new Post(postId++, title);
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
    public boolean updatePostTitle(int id, String title){
        return postRepository.update(id, title);
    }
}
