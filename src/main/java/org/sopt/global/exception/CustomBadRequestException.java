package org.sopt.global.exception;

import org.sopt.global.ErrorCode;

public class CustomBadRequestException extends RuntimeException{

    private final ErrorCode errorCode;

    public CustomBadRequestException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
