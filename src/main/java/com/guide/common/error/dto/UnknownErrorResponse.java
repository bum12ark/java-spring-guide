package com.guide.common.error.dto;

import com.guide.common.error.ErrorCode;

public class UnknownErrorResponse extends BaseErrorResponse {

    public UnknownErrorResponse() {
        super(
                ErrorCode.UNKNOWN.getHttpStatus().value(),
                ErrorCode.UNKNOWN.getCode(),
                ErrorCode.UNKNOWN.getMessage());
    }
}
