package com.guide.domain.account;

import com.guide.domain.account.entity.Account;
import com.guide.domain.model.User;

public class AccountBuilder {

    private static final String NAME = "하나";
    private static final Long USER_ID = 10L;
    private static final String USER_EMAIL = "mike@gmail.com";

    public static Account build() {
        User user = new User(USER_ID, USER_EMAIL);
        return Account.of(NAME, user);
    }
}
