package org.sopt.global;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sopt.dto.response.ErrorResponse;
import org.sopt.exception.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // 400 - 잘못된 요청
    @ExceptionHandler(CustomBadRequestException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(CustomBadRequestException e) {
        return buildErrorResponse(e.getErrorCode());
    }

    // 401 - 로그인 안한 사용자
    @ExceptionHandler(UnauthenticatedException.class)
    public ResponseEntity<ErrorResponse> handleUnAuthentication(UnauthenticatedException e) {
        return buildErrorResponse(e.getErrorCode());
    }

    // 403 - 접근 권한 없음
    @ExceptionHandler(CustomAccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(CustomAccessDeniedException e) {
        return buildErrorResponse(e.getErrorCode());
    }

    // 404 - not found
    @ExceptionHandler(CustomNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoSuchElement(CustomNotFoundException e) {
        return buildErrorResponse(e.getErrorCode());
    }

    // 400
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException e) {
        String message = ErrorCode.INVALID_INPUT_VALUE.getMessage();
        if (e.getBindingResult().hasErrors()) {
            var fieldError = e.getBindingResult().getFieldError();
            if (fieldError != null) {
                message = fieldError.getDefaultMessage();
            }
        }
        ErrorCode errorCode = ErrorCode.INVALID_INPUT_VALUE;
        ErrorResponse errorResponse = new ErrorResponse(false, errorCode.getStatus(), errorCode.getCode(), message);
        return ResponseEntity.status(errorCode.getStatus()).body(errorResponse);
    }

    // 500
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        log.error(e.getMessage());
        return buildErrorResponse(ErrorCode.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<ErrorResponse> buildErrorResponse(ErrorCode errorCode) {
        ErrorResponse errorResponse = ErrorResponse.from(errorCode);
        return ResponseEntity.status(errorCode.getStatus()).body(errorResponse);
    }
}

