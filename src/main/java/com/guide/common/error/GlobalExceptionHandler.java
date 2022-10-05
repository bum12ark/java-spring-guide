package com.guide.common.error;

import com.guide.common.error.dto.BaseErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

@Slf4j
@Order(Ordered.LOWEST_PRECEDENCE)
@RestControllerAdvice
public class GlobalExceptionHandler {

    // @RequestBody의 @Valid 하지 않을 경우
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<BaseErrorResponse> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException exception) {
        log.warn("MethodArgumentNotValidException occurred.", exception);
        return ResponseEntity.badRequest().body(BaseErrorResponse.of(ErrorCode.BAD_REQUEST));
    }

    // 지원 되지 않는 Request Method 요청
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity<BaseErrorResponse> handleMethodNotSupportException(
            HttpRequestMethodNotSupportedException exception) {
        log.warn("HttpRequestMethodNotSupportedException occurred.", exception);
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(BaseErrorResponse.of(ErrorCode.METHOD_NOT_ALLOWED));
    }

    // 존재하지 않는 API 요청
    @ExceptionHandler(NoHandlerFoundException.class)
    protected ResponseEntity<BaseErrorResponse> handleNoHandlerFoundException(
            NoHandlerFoundException exception) {
        log.warn("NoHandlerFoundException occurred.", exception);
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(BaseErrorResponse.of(ErrorCode.NOT_HANDLER_FOUND));
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<BaseErrorResponse> handleUncaughtException(Exception exception) {
        log.warn("UncaughtException occurred.", exception);
        return ResponseEntity.internalServerError().body(BaseErrorResponse.of(ErrorCode.UNKNOWN));
    }
}
