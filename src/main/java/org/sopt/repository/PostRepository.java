package org.sopt.repository;

import org.sopt.domain.Post;
import org.sopt.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

// CRUD
@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findByTitleContainingIgnoreCase(String keyword);
    List<Post> findByUserNameContainingIgnoreCase(String keyword);
    boolean existsByTitle(String title);
    boolean existsByTitleAndIdNot(String title, Long id);
    Optional<Post> findTopByUserOrderByCreatedDateDesc(User user);
}
