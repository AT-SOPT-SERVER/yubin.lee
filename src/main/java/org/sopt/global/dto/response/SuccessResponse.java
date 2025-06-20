package org.sopt.global.dto.response;

import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;

public record SuccessResponse<T>(
        int code,
        String message,
        @Nullable
        T data
) {
    // data 있는 경우
    public SuccessResponse(String message, T data) {
        this(HttpStatus.OK.value(), message, data);
    }

    // data 없는 경우
    public SuccessResponse(String message) {
        this(HttpStatus.OK.value(), message, null);
    }
}
