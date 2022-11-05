package com.guide.domain.account.application;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import com.guide.domain.account.AccountBuilder;
import com.guide.domain.account.dao.AccountRepository;
import com.guide.domain.account.entity.Account;
import com.guide.domain.account.error.AccountNotFoundException;
import com.guide.test.MockTest;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

class AccountFindServiceTest extends MockTest {

    @Mock private AccountRepository accountRepository;
    @InjectMocks private AccountFindService accountFindService;
    private Account account;

    @BeforeEach
    void beforeEach() {
        account = AccountBuilder.build();
    }

    @Nested
    class SuccessfulTest {

        @Test
        @DisplayName("단일 계좌 조회 성공")
        void should_return_single_account_when_method_called() {
            // GIVEN
            given(accountRepository.findById(anyLong())).willReturn(Optional.of(account));

            // WHEN
            final Account findAccount = accountFindService.findAccount(anyLong());

            // THEN
            assertNotNull(findAccount);
            assertEquals(findAccount.getBalance(), account.getBalance());
            assertEquals(findAccount.getName(), account.getName());
        }
    }

    @Nested
    class UnsuccessfulTest {

        @Test
        @DisplayName("단일 계좌 조회 시 존재하지 않는 계좌일 경우 AccountNotFoundException을 발생합니다.")
        void should_return_accountNotFoundException_when_not_exist_account_id() {
            // GIVEN
            given(accountRepository.findById(anyLong())).willReturn(Optional.empty());

            // WHEN & THEN
            assertThrows(AccountNotFoundException.class, () -> accountFindService.findAccount(anyLong()));
        }
    }
}
