package com.guide.domain.account.error;

import com.guide.common.error.ErrorCode;
import com.guide.common.error.dto.BaseErrorResponse;
import com.guide.domain.account.api.AccountController;
import com.guide.infra.client.error.UserNotFoundException;
import feign.FeignException;
import feign.RetryableException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice(basePackageClasses = AccountController.class)
public class AccountErrorHandler {

    @ExceptionHandler(UserNotFoundException.class)
    protected ResponseEntity<BaseErrorResponse> handleUserNotFoundException(
            UserNotFoundException exception) {
        log.warn("UserNotFoundException occurred.", exception);
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(BaseErrorResponse.of(ErrorCode.USER_NOT_FOUND));
    }

    @ExceptionHandler(AccountNotFoundException.class)
    protected ResponseEntity<BaseErrorResponse> handleAccountNotFoundException(
            AccountNotFoundException exception) {
        log.warn("AccountNotFoundException occurred.", exception);
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(BaseErrorResponse.of(ErrorCode.ACCOUNT_NOT_FOUND.getCode(), exception.getMessage()));
    }

    @ExceptionHandler(RetryableException.class)
    protected ResponseEntity<BaseErrorResponse> handleRetryableException(
            RetryableException exception) {
        log.warn("RetryableException occurred.", exception);
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                .body(BaseErrorResponse.of(ErrorCode.RETRY_MAX_ATTEMPTS));
    }

    @ExceptionHandler(FeignException.class)
    protected ResponseEntity<BaseErrorResponse> handleFeignException(FeignException exception) {
        log.warn("FeignException occurred.", exception);
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                .body(BaseErrorResponse.of(ErrorCode.USER_CLIENT_UNPROCESSABLE));
    }
}
