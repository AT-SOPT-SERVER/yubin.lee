package org.sopt.exception;

import org.springframework.http.HttpStatus;

public enum ErrorResponse {

    NOT_FOUND_USER_MSG(HttpStatus.NOT_FOUND.value(), "", "");

    private final int status;
    private final String code;
    private final String message;

    ErrorResponse(int status, String code, String message){
        this.status = status;
        this.code = code;
        this.message = message;
    }

    public int getStatus() {
        return this.status;
    }
}
