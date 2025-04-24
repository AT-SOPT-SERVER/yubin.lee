package org.sopt.dto.response;

import org.springframework.http.HttpStatus;

public record ErrorResponse(int status, String message) {
    public static ErrorResponse from(HttpStatus status, String message){
        return new ErrorResponse(status.value(), message);
    }
}
