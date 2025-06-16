package org.sopt.domain.post.dto.response;

import lombok.Builder;
import org.sopt.domain.post.model.PostComment;

import java.time.LocalDateTime;

@Builder
public record CommentResponse(
        Long commentId,
        String comment,
        String author,
        LocalDateTime createAt,
        LocalDateTime updateAt
) {
    public static CommentResponse from(PostComment postComment) {
        return CommentResponse.builder()
                .commentId(postComment.getId())
                .comment(postComment.getComment())
                .author(postComment.getUser().getName())
                .createAt(postComment.getCreatedDate())
                .updateAt(postComment.getModifiedDate())
                .build();
    }
}
