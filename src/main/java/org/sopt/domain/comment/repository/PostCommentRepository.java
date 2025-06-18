package org.sopt.domain.comment.repository;

import org.sopt.domain.comment.domain.PostComment;
import org.sopt.domain.post.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostCommentRepository extends JpaRepository<PostComment, Long> {

    List<PostComment> findByPost(Post post);
    Optional<PostComment> findByIdAndUserIdAndPostId(Long id, Long userId, Long postId);
}
