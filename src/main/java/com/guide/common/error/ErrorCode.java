package com.guide.common.error;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@JsonFormat(shape = Shape.OBJECT)
public enum ErrorCode {
    // Common
    BAD_REQUEST(4001L, "Invalid Input Value"),
    METHOD_NOT_ALLOWED(4002L, "Request Method Not Supported"),
    NOT_HANDLER_FOUND(4003L, "Non-Existent Api Request"),
    UNKNOWN(9999L, "Unknown Error"),

    // Feign
    RETRY_MAX_ATTEMPTS(5001L, "Max Retry Fail"),

    // User
    USER_NOT_FOUND(4100L, "User not found during call user-api"),
    USER_CLIENT_UNPROCESSABLE(5002L, "Something wrong during call user-api");

    private final long code;
    private final String message;
}
