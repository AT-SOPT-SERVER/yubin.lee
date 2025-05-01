package org.sopt.dto.response;

import org.sopt.domain.Post;

import java.time.LocalDateTime;

public record PostDetailResponseDto(Long id, String title, String content, String userName, LocalDateTime time) {
    public static PostDetailResponseDto from(Post post){
        return new PostDetailResponseDto(post.getId(), post.getTitle(), post.getContent(), post.getUser().getName(), post.getTime());
    }
}
