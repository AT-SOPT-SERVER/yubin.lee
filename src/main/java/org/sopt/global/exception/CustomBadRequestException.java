package org.sopt.global.exception;

import lombok.Getter;
import org.sopt.global.enums.ErrorCode;

@Getter
public class CustomBadRequestException extends RuntimeException{

    private final ErrorCode errorCode;

    public CustomBadRequestException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
