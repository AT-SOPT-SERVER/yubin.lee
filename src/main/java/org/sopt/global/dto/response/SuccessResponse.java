package org.sopt.global.dto.response;

import org.springframework.http.HttpStatus;

public record SuccessResponse<T>(int code, T data) {
    public SuccessResponse(T data){
        this(HttpStatus.OK.value(), data);
    }
}
