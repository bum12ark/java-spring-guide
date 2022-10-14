package com.guide.domain.account.application;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.guide.domain.account.dao.AccountRepository;
import com.guide.domain.account.dto.AccountCreateRequest;
import com.guide.domain.account.entity.Account;
import com.guide.infra.client.UserClient;
import com.guide.infra.client.error.UserNotFoundException;
import com.guide.test.MockTest;
import com.guide.test.setup.entity.AccountSetup;
import com.guide.test.setup.request.AccountCreateRequestSetup;
import com.guide.test.setup.response.UserClientSetup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

class AccountCreateServiceTest extends MockTest {

    @Mock private AccountRepository accountRepository;
    @Mock private UserClient userClient;
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
            Long userId = account.getUser().getId();
            AccountCreateRequest accountCreateRequest =
                    AccountCreateRequestSetup.build(userId, account.getName());
            given(accountRepository.save(any(Account.class))).willReturn(account);
            given(userClient.requestUserById(userId))
                    .willReturn(UserClientSetup.buildUserFindApiResponse(userId));

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
    class UnsuccessfulTests {

        @Test
        void 계좌_생성_생성_실패_없는_회원() {
            // GIVEN
            Long userId = account.getUser().getId();
            AccountCreateRequest accountCreateRequest =
                    AccountCreateRequestSetup.build(userId, account.getName());
            given(userClient.requestUserById(userId)).willThrow(new UserNotFoundException());

            // WHEN
            // THEN
            assertThrows(
                    UserNotFoundException.class, () -> accountCreateService.create(accountCreateRequest));
        }
    }
}
