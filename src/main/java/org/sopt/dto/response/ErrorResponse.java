package org.sopt.dto.response;

import org.sopt.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public record ErrorResponse(Boolean success, int status, String code, String message) {
    public static ErrorResponse from(ErrorCode errorCode){
        return new ErrorResponse(false, errorCode.getStatus(), errorCode.getCode(), errorCode.getMessage());
    }
}
