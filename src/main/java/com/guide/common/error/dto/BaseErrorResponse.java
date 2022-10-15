package com.guide.common.error.dto;

import com.guide.common.error.ErrorCode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BaseErrorResponse {
    private long errorCode;
    private String errorMessage;

    public static BaseErrorResponse of(ErrorCode errorCode) {
        BaseErrorResponse errorResponse = new BaseErrorResponse();
        errorResponse.errorCode = errorCode.getCode();
        errorResponse.errorMessage = errorCode.getMessage();
        return errorResponse;
    }

    public static BaseErrorResponse of(long errorCode, String errorMessage) {
        BaseErrorResponse errorResponse = new BaseErrorResponse();
        errorResponse.errorCode = errorCode;
        errorResponse.errorMessage = errorMessage;
        return errorResponse;
    }
}
