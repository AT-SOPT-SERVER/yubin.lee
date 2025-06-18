package org.sopt.domain.post.dto.response;

import org.sopt.domain.post.model.Post;

import java.time.LocalDateTime;

public record PostListsDto(
        Long id,
        String title,
        String userName,
        LocalDateTime createdAt
) {
    public static PostListsDto from(Post post) {
        return new PostListsDto(post.getId(), post.getTitle(), post.getUser().getName(), post.getCreatedDate());
    }
}
