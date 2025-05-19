package org.sopt.exception;

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
