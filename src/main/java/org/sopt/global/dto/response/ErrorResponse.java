package org.sopt.global.dto.response;

import org.sopt.global.ErrorCode;

public record ErrorResponse(
        int status,
        String message
) {
    public static ErrorResponse from(ErrorCode errorCode){
        return new ErrorResponse(errorCode.getStatus(), errorCode.getMessage());
    }
}
