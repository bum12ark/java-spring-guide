package com.guide.common.error.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class BaseErrorResponse {

    @JsonIgnore private final int status;
    private final long errorCode;
    private final String errorMessage;
}
