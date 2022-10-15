package com.guide.domain.account.error;

import com.guide.common.error.ErrorCode;

public class AccountNotFoundException extends RuntimeException {

    public AccountNotFoundException(long accountId) {
        super(String.format(ErrorCode.ACCOUNT_NOT_FOUND.getMessage(), accountId));
    }
}
