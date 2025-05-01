package org.sopt.dto.response;

import org.sopt.domain.Post;

import java.time.LocalDateTime;

public record PostAllResponseDto(Long id, String title, String userName, LocalDateTime time) {
    public static PostAllResponseDto from(Post post) {
        return new PostAllResponseDto(post.getId(), post.getTitle(), post.getUser().getName(), post.getTime());
    }
}
