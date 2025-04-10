package org.sopt.repository;

import org.sopt.domain.Post;
import java.util.ArrayList;
import java.util.List;

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

    public boolean update(int id, String title){
        Post post = findPostById(id);
        if (post!= null) {
            post.setTitle(title);
            return true;
        }
        return false;
    }

    public List<Post> search(String keyword){
        return postList.stream().filter(post -> post.getTitle().toLowerCase().contains(keyword.toLowerCase())).toList();
    }

    public boolean existsPostByTitle(String title){
        return postList.stream().anyMatch(post -> post.getTitle().equals(title));
    }

    public boolean existsPostByTitle(int id, String title){
        return postList.stream().anyMatch(post -> post.getId() == id && post.getTitle().equals(title));
    }
}
