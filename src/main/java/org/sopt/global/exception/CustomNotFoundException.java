package org.sopt.global.exception;

import org.sopt.global.ErrorCode;

public class CustomNotFoundException extends RuntimeException {
    private final ErrorCode errorCode;

    public CustomNotFoundException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
