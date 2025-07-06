package org.sopt.domain.user.dto.jwt;
import org.sopt.domain.user.domain.User;

public record UserDetails(
        String loginId,
        long userId
){
    public static UserDetails from(User user) {
        return new UserDetails(user.getLoginId(), user.getId());
    }

    public static UserDetails of(String loginId, long userId) {
        return new UserDetails(loginId, userId);
    }
}

