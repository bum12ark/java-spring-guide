package com.guide.domain.account.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.guide.common.error.ErrorCode;
import com.guide.domain.account.dto.AccountCreateRequest;
import com.guide.domain.account.entity.Account;
import com.guide.infra.client.WiremockStubFindUser;
import com.guide.test.IntegrationTest;
import com.guide.test.setup.entity.AccountSetup;
import com.guide.test.setup.request.AccountCreateRequestSetup;
import com.guide.test.setup.response.UserClientSetup;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

class AccountControllerTest extends IntegrationTest {

    private static final Long TEST_USER_ID = 1L;
    private static final String TEST_NAME = "하나은행";
    private static final String CREATE_ACCOUNT_URL = "/api/account";
    private static final String FIND_ACCOUNT_URL = "/api/account/%s";
    @Autowired private AccountSetup accountSetup;

    @Nested
    @WireMockTest(httpPort = 8443)
    class AccountCreateSuccessfulTest {

        @Test
        public void 계좌_생성_성공() throws Exception {
            // GIVEN
            AccountCreateRequest accountCreateRequest =
                    AccountCreateRequestSetup.build(TEST_USER_ID, TEST_NAME);
            String expectedJsonResponse =
                    objectMapper.writeValueAsString(
                            UserClientSetup.buildUserFindApiResponse(accountCreateRequest.userId()));
            WiremockStubFindUser.stubFindUser(accountCreateRequest.userId(), expectedJsonResponse);

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
            WiremockStubFindUser.verify(1, accountCreateRequest.userId());
        }
    }

    @Nested
    @WireMockTest(httpPort = 8443)
    class AccountCreateUnsuccessfulTest {

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

        @Test
        public void 계좌_생성_존재하지_않는_유저() throws Exception {
            // GIVEN
            AccountCreateRequest accountCreateRequest =
                    AccountCreateRequestSetup.build(TEST_USER_ID, TEST_NAME);
            WiremockStubFindUser.stubFindUserThrowsNotFoundException(accountCreateRequest.userId());

            // WHEN
            final ResultActions resultActions = requestCreateAccount(accountCreateRequest);

            // THEN
            resultActions
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("errorCode").value(ErrorCode.USER_NOT_FOUND.getCode()))
                    .andExpect(jsonPath("errorMessage").value(ErrorCode.USER_NOT_FOUND.getMessage()));
            WiremockStubFindUser.verify(1, accountCreateRequest.userId());
        }

        @Test
        public void 계좌_생성_유저_API_커넥션_타임아웃() throws Exception {
            // GIVEN
            AccountCreateRequest accountCreateRequest =
                    AccountCreateRequestSetup.build(TEST_USER_ID, TEST_NAME);
            WiremockStubFindUser.stubFindUserThrowsConnectionTimeout(accountCreateRequest.userId());

            // WHEN
            final ResultActions resultActions = requestCreateAccount(accountCreateRequest);

            // THEN
            resultActions
                    .andExpect(status().isBadGateway())
                    .andExpect(jsonPath("errorCode").value(ErrorCode.RETRY_MAX_ATTEMPTS.getCode()))
                    .andExpect(jsonPath("errorMessage").value(ErrorCode.RETRY_MAX_ATTEMPTS.getMessage()));
            WiremockStubFindUser.verify(3, accountCreateRequest.userId());
        }

        @Test
        public void 계좌_생성_유저_API_서버에러() throws Exception {
            // GIVEN
            AccountCreateRequest accountCreateRequest =
                    AccountCreateRequestSetup.build(TEST_USER_ID, TEST_NAME);
            WiremockStubFindUser.stubFindUserThrowsServerUnavailable(accountCreateRequest.userId());

            // WHEN
            final ResultActions resultActions = requestCreateAccount(accountCreateRequest);

            // THEN
            resultActions
                    .andExpect(status().isBadGateway())
                    .andExpect(jsonPath("errorCode").value(ErrorCode.USER_CLIENT_UNPROCESSABLE.getCode()))
                    .andExpect(
                            jsonPath("errorMessage").value(ErrorCode.USER_CLIENT_UNPROCESSABLE.getMessage()));
            WiremockStubFindUser.verify(1, accountCreateRequest.userId());
        }
    }

    @Nested
    class FindAccountSuccessfulTest {

        @Test
        @DisplayName("단일 계좌 조회 성공")
        void should_return_account_response_when_api_called() throws Exception {
            // GIVEN
            final Account account = accountSetup.save();

            // WHEN
            final ResultActions resultActions = requestFindAccount(account.getId());

            // THEN
            resultActions
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("id").value(account.getId()))
                    .andExpect(jsonPath("name").value(account.getName()))
                    .andExpect(jsonPath("balance").value(account.getBalance()))
                    .andExpect(jsonPath("user.id").value(account.getUser().getId()))
                    .andExpect(jsonPath("user.email").value(account.getUser().getEmail()));
        }
    }

    @Nested
    class FindAccountUnsuccessfulTest {

        @Test
        void should_return_404_not_found_when_not_exist_account_id() throws Exception {
            // GIVEN
            final long notExistAccountId = 9999L;

            // WHEN
            final ResultActions resultActions = requestFindAccount(notExistAccountId);

            // THEN
            resultActions
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("errorCode").value(ErrorCode.ACCOUNT_NOT_FOUND.getCode()))
                    .andExpect(
                            jsonPath("errorMessage")
                                    .value(
                                            String.format(ErrorCode.ACCOUNT_NOT_FOUND.getMessage(), notExistAccountId)));
        }
    }

    private ResultActions requestCreateAccount(final AccountCreateRequest accountCreateRequest)
            throws Exception {
        return mockMvc
                .perform(
                        post(CREATE_ACCOUNT_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(accountCreateRequest)))
                .andDo(print());
    }

    private ResultActions requestFindAccount(final long accountId) throws Exception {
        return mockMvc.perform(get(String.format(FIND_ACCOUNT_URL, accountId))).andDo(print());
    }
}
