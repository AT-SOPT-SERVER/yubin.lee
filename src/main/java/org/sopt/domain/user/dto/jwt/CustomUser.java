package org.sopt.domain.user.dto.jwt;
import org.sopt.domain.user.model.User;

public record CustomUser(
        String loginId,
        long userId
){
    public static CustomUser from(User user) {
        return new CustomUser(user.getLoginId(), user.getId());
    }

    public static CustomUser of(String loginId, long userId) {
        return new CustomUser(loginId, userId);
    }
}

