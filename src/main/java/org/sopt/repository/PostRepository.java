package org.sopt.repository;

import org.sopt.domain.Post;

import java.util.ArrayList;
import java.util.List;

// CRUD
public class PostRepository {
    List<Post> postList = new ArrayList<>();

    public void save(Post post){
        postList.add(post);
    }

    public List<Post> findAll(){
        return postList;
    }

    public Post findPostById(int id){
        return postList.stream()
                .filter(post -> post.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public boolean deletePostById(int id){
        return postList.removeIf(post -> post.getId() == id);
    }

}
