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
        for (Post post : postList) {
            if (post.getId() == id) {
                return post;
            }
        }
        return null;
    }

    public boolean deletePostById(int id){
        for (Post post : postList) {
            if (post.getId() == id) {
                postList.remove(id);
                return true;
            }
        }
        return false;
    }

    public boolean update(int id, String title){
        Post post = findPostById(id);
        if (post!= null) {
            post.setTitle(title);
            return true;
        }
        return false;
    }
}
