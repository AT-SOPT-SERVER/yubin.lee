package org.sopt.global.exception;

import lombok.Getter;
import org.sopt.global.ErrorCode;

@Getter
public class UnauthenticatedException extends RuntimeException{

    private final ErrorCode errorCode;

    public UnauthenticatedException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
