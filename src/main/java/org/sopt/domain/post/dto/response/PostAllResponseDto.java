package org.sopt.domain.post.dto.response;

import org.sopt.domain.post.model.Post;

import java.time.LocalDateTime;

public record PostAllResponseDto(
        Long id,
        String title,
        String userName,
        LocalDateTime createdAt
) {
    public static PostAllResponseDto from(Post post) {
        return new PostAllResponseDto(post.getId(), post.getTitle(), post.getUser().getName(), post.getCreatedDate());
    }
}
