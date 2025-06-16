package org.sopt.global.dto;

public record SuccessResponse<T>(Boolean success, T data) {
    public SuccessResponse(T data){
        this(true, data);
    }
}
