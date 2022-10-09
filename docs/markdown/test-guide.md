# 테스트 가이드

> 스프링에서는 여러 테스트 프레임워크를 지원합니다.
해당 리포지토리에서는 `JUnit5`를 기준으로 설명합니다.
>

## Integration Test (통합 테스트), E2E(End to End) 테스트

사용자의 입장에서 사용자가 사용하는 상황을 가정하고 테스트

### 장점

- 스프링의 모든 Bean을 올리고 테스트를 진행하기 때문에 운영환경과 가장 유사하게 테스트 가능

### 단점

- 스프링의 모든 Bean을 올리고 테스트를 진행하기 때문에 테스트 시간이 오래 걸립니다.
- 외부 API 콜백과 같은 테스트에 의존적입니다.
    - 이 부분은 `wiremock`을 사용하여 해결할 수 있습니다.

### Code

```java
@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class IntegrationTest {

    @Autowired protected MockMvc mockMvc;
    @Autowired protected ObjectMapper objectMapper;
}
```

- 통합 테스트의 Base 클래스
- 통합 테스트는 컨트롤러 테스트로 하며 요청부터 응답까지의 전체 플로우를 테스트합니다.
- `@ActiveProfiles` 어노테이션으로 테스트 프로파일을 설정합니다.
- `@Transactional` 어노테이션을 추가하여 테스트코드의 데이터베이스 정보가 자동으로 Rollback 됩니다.

### Test Code

```java
class AccountControllerTest extends IntegrationTest {

    private static final Long TEST_USER_ID = 1L;
    private static final String TEST_EMAIL = "mike@gmail.com";
    private static final String TEST_NAME = "하나은행";
    private static final String CREATE_ACCOUNT_URL = "/api/account";

    @Nested
    class SuccessfulTest {

        @Test
        public void 계좌_생성_성공() throws Exception {
            // GIVEN
            AccountCreateRequest accountCreateRequest =
                    AccountCreateRequestSetup.build(TEST_USER_ID, TEST_EMAIL, TEST_NAME);

            // WHEN
            ResultActions resultActions = requestCreateAccount(accountCreateRequest);

            // THEN
            resultActions
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("id").isNotEmpty())
                    .andExpect(jsonPath("name").value(TEST_NAME))
                    .andExpect(jsonPath("balance").value(0L))
                    .andExpect(jsonPath("user.id").isNotEmpty())
                    .andExpect(jsonPath("user.email").value(TEST_EMAIL));
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
                    Arguments.arguments(AccountCreateRequestSetup.build(null, TEST_EMAIL, TEST_NAME)),
                    Arguments.arguments(AccountCreateRequestSetup.build(TEST_USER_ID, null, TEST_NAME)),
                    Arguments.arguments(AccountCreateRequestSetup.build(TEST_USER_ID, TEST_EMAIL, null)));
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
```

- `IntegrationTest` 클래스를 상속받아 통일성을 유지합니다.
- 여러 메서드에서 사용하는 변수는 `private static final` 변수로 선언하여 사용하며 변수를 재사용합니다.
- GWT (Given, When, Then) 패턴을 사용하여 테스트 코드 포맷을 통일하며 비즈니스 요구사항에 좀 더 집중합니다.
- 요청에 대한 리퀘스트를 private 메서드로 분리하여 재사용성을 높힙니다.
- `@Nested` 어노테이션을 활용하여 성공 케이스와 실패케이스를 분리하여 가독성 있게 유지합니다. 이를 활용하면 성공 케이스와 실패 케이스를 따로 테스트 할 수 있습니다.
- `@ParameterizedTest`, `@MethodSource` 를 사용하여 하나의 테스트 메소드로 여러 개의 파라미터에 대해 테스트합니다.

## Wiremock

- 작성 예정

## Unit Test (Mocking)

하나의 모듈을 기준으로 독립적으로 진행되는 가장 작은 단위의 테스트

### 장점

- 진행하고자 하는 테스트에만 집중할 수 있습니다.
- Mocking을 통해 외부 의존성을 줄일 수 있습니다.

### 단점

- 의존성 있는 객체를 Mocking 하기 때문에 문제가 완결된 테스트는 아님

### Code

```java
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class MockTest {}
```

- 주로 Service 영역을 테스트합니다.
- `MockitoExtension`을 통해 Mock 테스트를 진행합니다.

### Test Code

```java
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
                    AccountCreateRequestSetup.build(
                            account.getUser().getId(), account.getUser().getEmail(), account.getName());
            given(accountRepository.save(any(Account.class))).willReturn(account);

            // WHEN
            Account account = accountCreateService.create(accountCreateRequest);

            // THEN
            assertEquals(account.getUser().getId(), accountCreateRequest.userId());
            assertEquals(account.getUser().getEmail(), accountCreateRequest.email());
            assertEquals(account.getBalance(), 0);
            assertEquals(account.getName(), accountCreateRequest.name());
        }
    }

    @Nested
    class UnsuccessfulTests {}
}
```

- `MockTest` 클래스를 상속받아 통일성을 높힙니다.
- `@Mock` 어노테이션을 통해 Mocking 할 객체를 선언합니다.
- `@InjectMocks` 어노테이션을 통해 Mocking한 객체를 주입받아 테스트할 서비스의 메서드를 실행합니다.
- `BDD (Behavior-Deriven Development)` 행위 주도 개발 방식인 `BDDMockito` 클래스의 `given().willReturn()`, `given().willThrow()` 메서드를 사용합니다.
    - 기본 패턴은 Given, When, Then 이며 테스트 대상의 상태 변화를 테스트하고 시나리오를 기반으로 테스트하는 패턴을 권장합니다.

## Repository Test (JPA)

- 작성 예정

## @Nested

- JUnit5 부터 나온 기능
- non-static 중첩 테스트 클래스를 의미합니다.
- @BeforeAll, @AfterAll 어노테이션이 사용 불가하며, 자체적으로 Test Instance LifeCycle을 가질 수 있습니다.
- 여러 테스트 그룹간의 관계를 표현할 수 있고 계층적으로 나타낼 수 있습니다.

### Code

```java
class fooTest() {

    @Nested
    class SuccessfulTests {

        @Test
        void something() {}
    }

    @Nested
    class UnsuccessfulTests {
    
        @Test
        void somethingWrong() {}
    }
}
```

## Reference

- [https://github.com/cheese10yun/spring-guide/blob/master/docs/test-guide.md](https://github.com/cheese10yun/spring-guide/blob/master/docs/test-guide.md)
- [https://junit.org/junit5/docs/current/user-guide/](https://junit.org/junit5/docs/current/user-guide/)