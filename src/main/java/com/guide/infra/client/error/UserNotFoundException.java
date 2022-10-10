package com.guide.infra.client.error;

import com.guide.common.error.ErrorCode;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException() {
        super(ErrorCode.USER_NOT_FOUND.getMessage());
    }
}
