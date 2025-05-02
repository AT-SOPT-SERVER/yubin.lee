package org.sopt.exception;

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
