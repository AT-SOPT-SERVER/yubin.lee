package org.sopt.service;

import org.sopt.domain.Post;
import org.sopt.repository.PostRepository;
import org.sopt.util.GeneratorId;

import java.util.List;

public class PostService {

    // postRepository 가져오기
    private final PostRepository postRepository = new PostRepository();

    // 게시글 저장
    public void createPost(String title) throws IllegalArgumentException{
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
        return postRepository.update(id, title);
    }

    // 게시글 찾기
    public List<Post> searchPostsByKeyword(String keyword){
        return postRepository.search(keyword);
    }

    // 중복된 게시물
    private void duplicatePost(String title){
        if (postRepository.existsPostByTitle(title)){
            throw new IllegalArgumentException("❌ 중복된 게시물입니다.");
        }
    }
}
