package org.sopt.global.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResponseMessage {

    CREATE_POST_SUCCESS("게시물이 저장되었습니다."),
    DELETE_POST_SUCCESS("게시물이 삭제되었습니다."),
    UPDATE_POST_SUCCESS("게시물이 수정되었습니다."),
    GET_POST_SUCCESS("게시물 리스트 조회에 성공했습니다."),
    GET_POST_DETAIL_SUCCESS("게시물 상세 조회에 성공했습니다."),
    SEARCH_POST_SUCCESS("게시물 검색에 성공했습니다."),
    CREATE_USER_SUCCESS("회원가입이 완료되었습니다."),
    LOGIN_SUCCESS("로그인에 성공했습니다."),
    LOGOUT_SUCCESS("로그아웃에 성공했습니다."),
    TOKEN_REFRESH_SUCCESS("토큰 재발급에 성공했습니다"),
    CREATE_COMMENT_SUCCESS("댓글이 등록되었습니다."),
    UPDATE_COMMENT_SUCCESS("댓글이 수정되었습니다."),
    DELETE_COMMENT_SUCCESS("댓글이 삭제되었습니다."),
    GET_COMMENT_SUCCESS("댓글 조회에 성공했습니다."),
    CREATE_POSTLIKE_SUCCESS("게시물 좋아요가 등록되었습니다."),
    CANCEL_POSTLIKE_SUCCESS("게시물 좋아요가 취소되었습니다."),
    COUNT_POSTLIKE_SUCCESS("게시물 좋아요 개수 반환에 성공했습니다."),
    GET_POSTLIKE_USERS_SUCCESS("게시물 좋아요 유저 리스트 반환에 성공했습니다."),
    CREATE_POST_COMMENT_LIKE_SUCCESS("댓글 좋아요가 등록되었습니다."),
    CANCEL_POST_COMMENT_LIKE_SUCCESS("댓글 좋아요가 취소되었습니다."),
    COUNT_COMMENT_LIKE_SUCCESS("댓글 좋아요 개수 반환에 성공했습니다.");

    private final String message;
}
