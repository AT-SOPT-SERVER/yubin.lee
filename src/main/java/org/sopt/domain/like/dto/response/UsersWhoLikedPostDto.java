package org.sopt.domain.like.dto.response;

import lombok.Builder;
import org.sopt.domain.user.domain.User;

@Builder
public record UsersWhoLikedPostDto(
        long userId,
        String userName
) {
    public static UsersWhoLikedPostDto from(final User user) {
        return UsersWhoLikedPostDto.builder()
                .userId(user.getId())
                .userName(user.getName())
                .build();
    }
}
