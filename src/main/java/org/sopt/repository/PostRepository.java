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

    List<Post> findAll();
    List<Post> findByTitleContainingIgnoreCase(String keyword);
    Optional<Post> findById(Long id);
    Optional<Post> findByIdAndUser(Long id, User user);
    boolean existsByTitle(String title);
    Optional<Post> findTopByOrderByTimeDesc();
    Optional<Post> findTopByUserOrderByTimeDesc(User user);

}
