package org.sopt.domain.post.repository;

import org.sopt.domain.post.domain.Post;
import org.sopt.domain.post.repository.custom.PostRepositoryCustom;
import org.sopt.domain.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

// CRUD
@Repository
public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom {

    boolean existsByTitle(String title);
    boolean existsByTitleAndIdNot(String title, Long id);
    Optional<Post> findTopByUserOrderByCreatedDateDesc(User user);
}
