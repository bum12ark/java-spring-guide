package com.guide.test.setup.entity;

import com.guide.domain.account.AccountBuilder;
import com.guide.domain.account.dao.AccountRepository;
import com.guide.domain.account.entity.Account;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("test")
@Component
public class AccountSetup {

    private final AccountRepository accountRepository;

    public AccountSetup(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account save() {
        Account account = AccountBuilder.build();
        return accountRepository.save(account);
    }
}
