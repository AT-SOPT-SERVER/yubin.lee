package org.sopt.global;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResponseMessage {

    CREATE_POST_SUCCESS("게시물이 저장되었습니다."),
    DELETE_POST_SUCCESS("게시물이 삭제되었습니다."),
    UPDATE_POST_SUCCESS("게시물이 수정되었습니다."),
    CREATE_USER_SUCCESS("회원가입이 완료되었습니다."),
    LOGIN_SUCCESS("로그인에 성공했습니다."),
    CREATE_COMMENT_SUCCESS("댓글이 등록되었습니다."),
    UPDATE_COMMENT_SUCCESS("댓글이 수정되었습니다."),
    DELETE_COMMENT_SUCCESS("댓글이 삭제되었습니다."),
    CREATE_POSTLIKE_SUCCESS("게시물 좋아요가 등록되었습니다."),
    CANCEL_POSTLIKE_SUCCESS("게시물 좋아요가 취소되었습니다."),
    CREATE_POST_COMMENT_LIKE_SUCCESS("댓글 좋아요가 등록되었습니다."),
    CANCEL_POST_COMMENT_LIKE_SUCCESS("댓글 좋아요가 취소되었습니다.");

    private final String message;
}
