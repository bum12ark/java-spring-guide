package com.guide.domain.account.application;

import com.guide.domain.account.dao.AccountRepository;
import com.guide.domain.account.dto.AccountCreateRequest;
import com.guide.domain.account.entity.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AccountCreateService {

    private final AccountRepository accountRepository;

    public Account create(final AccountCreateRequest createRequest) {
        Account account = createRequest.toEntity();
        return accountRepository.save(account);
    }
}
