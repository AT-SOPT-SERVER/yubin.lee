package org.sopt.domain.post.repository.custom;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.sopt.domain.post.domain.QPost;
import org.sopt.domain.post.dto.response.PostListsDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class PostRepositoryCustomImpl implements PostRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<PostListsDto> searchByWhere(String keyword, Pageable pageable) {
        QPost post = QPost.post;

        List<PostListsDto> content = queryFactory
                .select(Projections.constructor(PostListsDto.class, post.id, post.title, post.user.name, post.createdDate))
                .from(post)
                .where(
                        containsKeyword(keyword)
                )
                .fetch();

        return new PageImpl<>(content, pageable, content.size());
    }

    private BooleanExpression containsKeyword(String keyword){
        return keyword != null ? QPost.post.title.containsIgnoreCase(keyword) : null;
    }

}
