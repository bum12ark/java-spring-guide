package com.guide.common.error;

import com.guide.common.error.dto.BadRequestErrorResponse;
import com.guide.common.error.dto.BaseErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<BaseErrorResponse> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException exception) {
        log.warn("MethodArgumentNotValidException occurred.", exception);
        return ResponseEntity.badRequest().body(new BadRequestErrorResponse());
    }
}
