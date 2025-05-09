package org.sopt.dto.response;

import org.sopt.domain.Post;

import java.time.LocalDateTime;

public record PostResponseDto(Long id, String title, LocalDateTime time) {
    public static PostResponseDto from(Post post) {
        return new PostResponseDto(post.getId(), post.getTitle(), post.getTime());
    }
}
