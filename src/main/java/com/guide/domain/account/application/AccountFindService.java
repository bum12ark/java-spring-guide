package com.guide.domain.account.application;

import com.guide.domain.account.dao.AccountRepository;
import com.guide.domain.account.entity.Account;
import com.guide.domain.account.error.AccountNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class AccountFindService {

    private final AccountRepository accountRepository;

    public Account findAccount(final long accountId) {
        return accountRepository
                .findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException(accountId));
    }
}
