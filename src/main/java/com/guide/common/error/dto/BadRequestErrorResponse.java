package com.guide.common.error.dto;

import com.guide.common.error.ErrorCode;

public class BadRequestErrorResponse extends BaseErrorResponse {

    public BadRequestErrorResponse() {
        super(
                ErrorCode.BAD_REQUEST.getHttpStatus().value(),
                ErrorCode.BAD_REQUEST.getCode(),
                ErrorCode.BAD_REQUEST.getMessage());
    }
}
