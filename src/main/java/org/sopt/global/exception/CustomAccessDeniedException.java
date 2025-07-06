package org.sopt.global.exception;

import lombok.Getter;
import org.sopt.global.enums.ErrorCode;

@Getter
public class CustomAccessDeniedException extends RuntimeException {

    private final ErrorCode errorCode;

    public CustomAccessDeniedException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}

