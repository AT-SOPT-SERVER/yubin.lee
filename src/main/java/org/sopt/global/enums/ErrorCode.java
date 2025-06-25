package org.sopt.global.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    // COMMON
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST.value(), "요청값이 유효하지 않습니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value(), "서버 내부 오류 발생함!! 로그 확인하세용"),
    NOT_FOUND_PAGE(HttpStatus.NOT_FOUND.value(), "페이지를 찾을 수 없습니다."),

    // POST
    NOT_FOUND_POST(HttpStatus.NOT_FOUND.value(), "게시물을 찾을 수 없습니다."),
    POST_DUPLICATED(HttpStatus.BAD_REQUEST.value(), "게시물이 이미 존재합니다."),
    POST_CREATION_LIMIT(HttpStatus.BAD_REQUEST.value(), "게시물 작성은 3분 뒤에 가능합니다."),
    EMPTY_TITLE(HttpStatus.BAD_REQUEST.value(), "제목을 입력해야 합니다."),
    TITLE_TOO_LONG(HttpStatus.BAD_REQUEST.value(), "제목은 30자 이하여야 합니다."),
    EMPTY_CONTENT(HttpStatus.BAD_REQUEST.value(), "내용을 입력해야 합니다."),

    // USER
    UN_AUTHENTICATION(HttpStatus.UNAUTHORIZED.value(), "로그인이 필요합니다."),
    POST_ACCESS_DENIED(HttpStatus.FORBIDDEN.value(), "게시물에 대한 접근 권한이 없습니다."),
    COMMENT_WRITE_DENIED(HttpStatus.FORBIDDEN.value(), "댓글 쓰기 권한이 없습니다."),
    COMMENT_DELETE_DENIED(HttpStatus.FORBIDDEN.value(), "댓글 삭제 권한이 없습니다."),
    NOT_FOUND_USER(HttpStatus.NOT_FOUND.value(), "등록되지 않은 유저입니다."),
    NOT_FOUND_AUTHORITY(HttpStatus.NOT_FOUND.value(), "권한 정보가 없습니다."),
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED.value(), "리프레시 토큰이 유효하지 않습니다."),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED.value(),"만료된 토큰입니다."),
    UN_SUPPORTED_TOKEN(HttpStatus.UNAUTHORIZED.value(),"지원하지 않는 토큰입니다."),
    INVALID_SIGNATURE(HttpStatus.UNAUTHORIZED.value(), "잘못된 JWT 서명입니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED.value(),"유효하지 않은 JWT 입니다."),
    INVALID_TOKEN_OWNER(HttpStatus.UNAUTHORIZED.value(),"로그인한 사용자의 Refresh Token이 아닙니다."),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED.value(), "비밀번호가 일치하지 않습니다."),

    // COMMENT
    NOT_FOUND_COMMENT(HttpStatus.NOT_FOUND.value(), "댓글을 찾을 수 없습니다."),

    // LIKE
    NOT_FOUND_POST_LIKE(HttpStatus.NOT_FOUND.value(), "게시물 좋아요 기록을 찾을 수 없습니다."),
    COMMENT_LIKE_DELETE_FORBIDDEN(HttpStatus.FORBIDDEN.value(), "유저가 해당 댓글의 좋아요를 취소할 권한이 없습니다."),
    ALREADY_LIKED(HttpStatus.BAD_REQUEST.value(), "이미 좋아요를 눌렀습니다.");

    private final int status;
    private final String message;

    ErrorCode(int status, String message){
        this.status = status;
        this.message = message;
    }
}
