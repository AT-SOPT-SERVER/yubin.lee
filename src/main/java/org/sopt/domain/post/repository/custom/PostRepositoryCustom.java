package org.sopt.domain.post.repository.custom;

import org.sopt.domain.post.dto.response.PostListsDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostRepositoryCustom {

    Page<PostListsDto> searchByWhere(String keyword, Pageable pageable);
}
