package org.sopt.dto.response;

public record SuccessResponse<T>(Boolean success, T data) {
    public SuccessResponse(T data){
        this(true, data);
    }
}
