package org.sopt.global.exception;

import org.sopt.global.ErrorCode;

public class UnauthenticatedException extends RuntimeException{

    private final ErrorCode errorCode;

    public UnauthenticatedException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode(){
        return this.errorCode;
    }
}
