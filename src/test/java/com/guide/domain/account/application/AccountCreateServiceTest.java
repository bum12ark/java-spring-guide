package com.guide.domain.account.application;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.guide.domain.account.dao.AccountRepository;
import com.guide.domain.account.dto.AccountCreateRequest;
import com.guide.domain.account.entity.Account;
import com.guide.test.MockTest;
import com.guide.test.setup.entity.AccountSetup;
import com.guide.test.setup.request.AccountCreateRequestSetup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

class AccountCreateServiceTest extends MockTest {

    @Mock private AccountRepository accountRepository;
    @InjectMocks private AccountCreateService accountCreateService;
    private Account account;

    @BeforeEach
    void beforeEach() {
        account = AccountSetup.build();
    }

    @Nested
    class SuccessfulTest {

        @Test
        void 계좌_생성_성공() {
            // GIVEN
            AccountCreateRequest accountCreateRequest =
                    AccountCreateRequestSetup.build(account.getUser().getId(), account.getName());
            given(accountRepository.save(any(Account.class))).willReturn(account);

            // WHEN
            Account account = accountCreateService.create(accountCreateRequest);

            // THEN
            assertEquals(account.getUser().getId(), accountCreateRequest.userId());
            assertNotNull(account.getUser().getEmail());
            assertEquals(account.getBalance(), 0);
            assertEquals(account.getName(), accountCreateRequest.name());
        }
    }

    @Nested
    class UnsuccessfulTests {}
}
