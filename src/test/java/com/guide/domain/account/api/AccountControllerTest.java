package com.guide.domain.account.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.guide.domain.account.dto.AccountCreateRequest;
import com.guide.test.IntegrationTest;
import com.guide.test.setup.request.AccountCreateRequestSetup;
import java.util.stream.Stream;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

class AccountControllerTest extends IntegrationTest {

    private static final Long TEST_USER_ID = 1L;
    private static final String TEST_NAME = "하나은행";
    private static final String CREATE_ACCOUNT_URL = "/api/account";

    @Nested
    class SuccessfulTest {

        @Test
        public void 계좌_생성_성공() throws Exception {
            // GIVEN
            AccountCreateRequest accountCreateRequest =
                    AccountCreateRequestSetup.build(TEST_USER_ID, TEST_NAME);

            // WHEN
            ResultActions resultActions = requestCreateAccount(accountCreateRequest);

            // THEN
            resultActions
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("id").isNotEmpty())
                    .andExpect(jsonPath("name").value(TEST_NAME))
                    .andExpect(jsonPath("balance").value(0L))
                    .andExpect(jsonPath("user.id").isNotEmpty())
                    .andExpect(jsonPath("user.email").isNotEmpty());
        }
    }

    @Nested
    class UnsuccessfulTests {

        @ParameterizedTest
        @MethodSource(value = "createInvalidAccountCreateRequest")
        public void 계좌_생성_유효하지않은_입력값(final AccountCreateRequest accountCreateRequest) throws Exception {
            // WHEN
            final ResultActions resultActions = requestCreateAccount(accountCreateRequest);

            // THEN
            resultActions.andExpect(status().isBadRequest());
        }

        private static Stream<Arguments> createInvalidAccountCreateRequest() {
            return Stream.of(
                    Arguments.arguments(AccountCreateRequestSetup.build(null, TEST_NAME)),
                    Arguments.arguments(AccountCreateRequestSetup.build(TEST_USER_ID, null)));
        }
    }

    private ResultActions requestCreateAccount(AccountCreateRequest accountCreateRequest)
            throws Exception {
        return mockMvc
                .perform(
                        post(CREATE_ACCOUNT_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(accountCreateRequest)))
                .andDo(print());
    }
}
