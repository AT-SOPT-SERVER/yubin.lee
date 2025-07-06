package org.sopt.global.exception;

import lombok.Getter;
import org.sopt.global.enums.ErrorCode;

@Getter
public class CustomNotFoundException extends RuntimeException {
    private final ErrorCode errorCode;

    public CustomNotFoundException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
