package org.sopt.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {

    // COMMON
    NOT_FOUND(HttpStatus.NOT_FOUND.value(), "E001", "리소스를 찾을 수 없습니다."),
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST.value(), "E002", "요청값이 유효하지 않습니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value(), "E500", "서버 내부 오류 발생함!! 로그 확인하세용"),

    // POST
    POST_DUPLICATED(HttpStatus.BAD_REQUEST.value(), "P001", "게시물이 이미 존재합니다."),
    POST_CREATION_LIMIT(HttpStatus.BAD_REQUEST.value(), "P002", "게시물 작성은 3분 뒤에 가능합니다."),
    EMPTY_TITLE(HttpStatus.BAD_REQUEST.value(), "P003", "제목을 입력해야 합니다."),
    TITLE_TOO_LONG(HttpStatus.BAD_REQUEST.value(), "P004", "제목은 30자 이하여야 합니다."),
    EMPTY_CONTENT(HttpStatus.BAD_REQUEST.value(), "P005", "내용을 입력해야 합니다."),

    // USER
    UN_AUTHENTICATION(HttpStatus.UNAUTHORIZED.value(), "U001", "로그인이 필요합니다."),
    ACCESS_DENIED(HttpStatus.FORBIDDEN.value(), "U002", "게시물에 대한 접근 권한이 없습니다.");

    private final int status;
    private final String code;
    private final String message;

    ErrorCode(int status, String code, String message){
        this.status = status;
        this.code = code;
        this.message = message;
    }

    public int getStatus() {
        return this.status;
    }

    public String getCode(){
        return this.code;
    }

    public String getMessage(){
        return this.message;
    }
}
