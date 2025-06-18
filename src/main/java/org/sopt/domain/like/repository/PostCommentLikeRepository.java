package org.sopt.domain.like.repository;

import org.sopt.domain.like.model.PostCommentLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostCommentLikeRepository extends JpaRepository<PostCommentLike, Long> {

    long countByPostCommentId(long postCommentId);
    boolean existsByUserIdAndPostCommentId(long userId, long postCommentId);
    Optional<PostCommentLike> findByUserIdAndPostCommentId(long userId, long postCommentId);
}
