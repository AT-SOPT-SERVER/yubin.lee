package org.sopt.dto.response;

import lombok.Builder;
import org.sopt.domain.Comment;

import java.time.LocalDateTime;

@Builder
public record CommentResponse(
        Long commentId,
        String comment,
        String author,
        LocalDateTime createAt,
        LocalDateTime updateAt
) {
    public static CommentResponse from(Comment comment) {
        return CommentResponse.builder()
                .commentId(comment.getId())
                .comment(comment.getComment())
                .author(comment.getUser().getName())
                .createAt(comment.getCreatedDate())
                .updateAt(comment.getModifiedDate())
                .build();
    }
}
