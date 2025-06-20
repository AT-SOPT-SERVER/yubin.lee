package org.sopt.global.dto.response;

import lombok.Builder;
import org.sopt.global.enums.ErrorCode;

@Builder
public record ErrorResponse(
        int status,
        String message
) {
    public static ErrorResponse from(ErrorCode errorCode){
        return new ErrorResponse(errorCode.getStatus(), errorCode.getMessage());
    }
}
