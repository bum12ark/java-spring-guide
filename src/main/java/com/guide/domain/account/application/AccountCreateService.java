package com.guide.domain.account.application;

import com.guide.domain.account.dao.AccountRepository;
import com.guide.domain.account.dto.AccountCreateRequest;
import com.guide.domain.account.entity.Account;
import com.guide.domain.model.User;
import com.guide.infra.client.UserClient;
import com.guide.infra.client.dto.UserFindApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class AccountCreateService {

    private final UserClient userClient;
    private final AccountRepository accountRepository;

    @Transactional
    public Account create(final AccountCreateRequest createRequest) {
        UserFindApiResponse userFindApiResponse = userClient.requestUserById(createRequest.userId());
        User user =
                User.builder()
                        .id(userFindApiResponse.data().id())
                        .email(userFindApiResponse.data().email())
                        .build();
        Account account = Account.of(createRequest.name(), user);
        return accountRepository.save(account);
    }
}
