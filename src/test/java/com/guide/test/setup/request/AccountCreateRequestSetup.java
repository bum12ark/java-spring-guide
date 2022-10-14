package com.guide.test.setup.request;

import com.guide.domain.account.dto.AccountCreateRequest;

public class AccountCreateRequestSetup {

    private static final Long TEST_USER_ID = 1L;
    private static final String TEST_NAME = "하나은행";

    public static AccountCreateRequest build() {
        return build(TEST_USER_ID, TEST_NAME);
    }

    public static AccountCreateRequest build(Long userId, String name) {
        return new AccountCreateRequest(userId, name);
    }
}
