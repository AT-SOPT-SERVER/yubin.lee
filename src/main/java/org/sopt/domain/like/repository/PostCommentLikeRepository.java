package org.sopt.domain.like.repository;

import org.sopt.domain.like.domain.PostCommentLike;
import org.sopt.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostCommentLikeRepository extends JpaRepository<PostCommentLike, Long> {

    long countByPostCommentId(long postCommentId);
    boolean existsByUserIdAndPostCommentId(long userId, long postCommentId);
    Optional<PostCommentLike> findByUserIdAndPostCommentId(long userId, long postCommentId);

    @Query("SELECT pcl.user FROM PostCommentLike pcl WHERE pcl.postComment.id = :commentId")
    List<User> findUsersByPostCommentId(@Param("commentId") long commentId);
}
