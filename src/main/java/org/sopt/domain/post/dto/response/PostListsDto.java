package org.sopt.domain.post.dto.response;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.sopt.domain.post.domain.Post;

import java.time.LocalDateTime;

public record PostListsDto(
        Long id,
        String title,
        String userName,
        @JsonSerialize(using = LocalDateTimeSerializer.class)
        @JsonDeserialize(using = LocalDateTimeDeserializer.class)
        LocalDateTime createdAt
) {
    public static PostListsDto from(Post post) {
        return new PostListsDto(post.getId(), post.getTitle(), post.getUser().getName(), post.getCreatedDate());
    }
}
