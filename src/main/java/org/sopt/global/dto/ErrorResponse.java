package org.sopt.global.dto;

import org.sopt.global.ErrorCode;

public record ErrorResponse(
        Boolean success,
        int status,
        String code,
        String message
) {
    public static ErrorResponse from(ErrorCode errorCode){
        return new ErrorResponse(false, errorCode.getStatus(), errorCode.getCode(), errorCode.getMessage());
    }
}
