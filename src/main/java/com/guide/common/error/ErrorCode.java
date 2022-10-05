package com.guide.common.error;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@JsonFormat(shape = Shape.OBJECT)
public enum ErrorCode {
    BAD_REQUEST(4001L, "Invalid Input Value"),
    METHOD_NOT_ALLOWED(4002L, "Request Method Not Supported"),
    NOT_HANDLER_FOUND(4003L, "Non-Existent Api Request"),
    UNKNOWN(9999L, "Unknown Error");

    private final long code;
    private final String message;
}
