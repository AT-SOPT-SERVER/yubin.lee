package org.sopt.controller;

import org.sopt.domain.Post;
import org.sopt.service.PostService;
import org.sopt.util.Validation;

import java.util.ArrayList;
import java.util.List;

public class PostController {

    private final PostService postService = new PostService();

    public boolean createPost(String title){
        try {
            Validation.isTitleValid(title);
            postService.createPost(title);
            return true;
        } catch (IllegalArgumentException e){
            System.out.println(e.getMessage());
            return false;
        }
    }

    public List<Post> getAllPosts(){
        return postService.getAllPosts();
    }

    public Post getPostById(int id){
        return postService.getPostById(id);
    }

    public boolean deletePostById(int id){
        return postService.deletePostById(id);

    }

    public boolean updatePostTitle(int id, String title){
        try {
            Validation.isTitleValid(title);
            return postService.updatePostTitle(id, title);
        } catch (IllegalArgumentException e){
            System.out.println(e.getMessage());
            return false;
        }
    }

    public List<Post> searchPostsByKeyword(String keyword){
        // 구현 예정
        return new ArrayList<>();
    }
}
