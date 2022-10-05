package com.guide.common.error;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
@JsonFormat(shape = Shape.OBJECT)
public enum ErrorCode {
    BAD_REQUEST(HttpStatus.BAD_REQUEST, 4001L, "Invalid Input Value"),
    UNKNOWN(HttpStatus.INTERNAL_SERVER_ERROR, 9999L, "Unknown Error");

    private final HttpStatus httpStatus;
    private final long code;
    private final String message;
}
