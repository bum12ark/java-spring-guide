# 외부 API 통신 (Feign Client)

## Install Guide

```kotlin
dependencies {
    implementation("org.springframework.cloud:spring-cloud-starter-openfeign")
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:2021.0.4")
    }
}
```

## Application

```java
@EnableFeignClients
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```

- `@EnableFeignClients` 어노테이션으로 `@FeignClient` 어노테이션이 붙은 interface들을 스캔합니다.
- root package에 선언하지 않을 경우 basePackages를 통해 스캔할 패키지를 명시해줘야 합니다.

## FeignClient interface

```java
@FeignClient(
        name = "user-api",
        url = "${user.feign.base-url}",
        configuration = UserClientConfiguration.class)
public interface UserClient {

    @GetMapping("/api/users/{userId}")
    UserFindApiResponse requestUserById(@PathVariable("userId") long userId);
}
```

- name: feignClient 서비스 이름을 명시합니다.
- url: API 통신을 할 URL을 설정합니다. 여러 환경에서의 테스트를 위해 property 파일의 정보로 작성합니다.
- configuration: 커스터마이징한 configuration을 설정할 수 있습니다.

## Basic Configuration

- Feign Client에서는 기본적으로 제공하는 Configuration이 존재합니다. (FeignClientsConfiguration.class)
- 해당 Bean들은 `@ConditionalOnMissingBean` 어노테이션이 붙어있어 해당 Bean이 없을때 적용되는 Default라는 의미 입니다.

    ```java
    @Bean
    @ConditionalOnMissingBean
    public Decoder feignDecoder(ObjectProvider<HttpMessageConverterCustomizer> customizers) {
       return new OptionalDecoder(new ResponseEntityDecoder(new SpringDecoder(messageConverters, customizers)));
    }
    ```


## Custom Configuration

```java
@RequiredArgsConstructor
public class UserClientConfiguration {

    private static final String CONNECTION_TIMEOUT_MILLIS = "${user.feign.connection-timeout-millis}";
    private static final String READ_TIMEOUT_MILLIS = "${user.feign.read-timeout-millis}";
    private static final String RETRY_PERIOD_MILLIS = "${user.feign.retry.period-millis}";
    private static final String RETRY_MAX_PERIOD_MILLIS = "${user.feign.retry.max-period-millis}";
    private static final String RETRY_MAX_ATTEMPTS = "${user.feign.retry.attempts}";

    private final ObjectMapper objectMapper;

    @Bean
    public Encoder feignEncoder() {
        return new SpringEncoder(buildMessageConverters());
    }

    @Bean
    public Decoder feignDecoder() {
        return new SpringDecoder(buildMessageConverters());
    }

    @Bean
    public Contract contract() {
        return new SpringMvcContract();
    }

    @Bean
    public ErrorDecoder decoder() {
        return new UserClientErrorDecoder();
    }

    @Bean
    public Request.Options options(
            @Value(CONNECTION_TIMEOUT_MILLIS) int connectionTimeoutMillis,
            @Value(READ_TIMEOUT_MILLIS) int readTimeoutMillis) {
        return new Options(
                connectionTimeoutMillis,
                TimeUnit.MILLISECONDS,
                readTimeoutMillis,
                TimeUnit.MILLISECONDS,
                false);
    }

    @Bean
    public Retryer retryer(
            @Value(RETRY_PERIOD_MILLIS) int period,
            @Value(RETRY_MAX_PERIOD_MILLIS) int maxPeriod,
            @Value(RETRY_MAX_ATTEMPTS) int maxAttempts) {
        return new Default(period, maxPeriod, maxAttempts);
    }

    @Bean
    public Logger.Level logger() {
        return Level.FULL;
    }

    private ObjectFactory<HttpMessageConverters> buildMessageConverters() {
        MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter =
                new MappingJackson2HttpMessageConverter(objectMapper);
        return () -> new HttpMessageConverters(mappingJackson2HttpMessageConverter);
    }
}
```

- application.yaml 파일 방식으로의 Configuration 구성 또한 지원합니다.

### Encoder

- Default: SpringEncoder
- Feign으로 요청할 때 요청에 대해서 인코딩 처리를 하는 부분
- HttpMessageConverters를 이용하여 응답의 Type에 맞게 처리해주는 것으로 설정하며 커스텀한 ObjecMapper를 사용하도록 할 수 있습니다.

### Decoder

- Default: RespnseEntityDecoder
- Feign으로 호출하고 난 후 HTTP 응답에 대해 디코딩 처리를 하는 부분
- HttpMessageConverters를 이용하여 응답의 Type에 맞게 처리해주는 것으로 설정하며 커스텀한 ObjecMapper를 사용하도록 할 수 있습니다.
- **ObjectMapper Example**

    ```java
    @Configuration
    public class JacksonConfig {
    
        @Bean
        public ObjectMapper objectMapper() {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule()); // Java8 LocalDateTime Module
            objectMapper.setSerializationInclusion(Include.NON_EMPTY); // null or empty not Serialization
            objectMapper.enable(SerializationFeature.INDENT_OUTPUT); // console pretty print
            objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
            return objectMapper;
        }
    }
    ```


### Contract

- Default: SpringMvcContract
- FeignClient의 모든 어노테이션을 SpringMvc에서 사용하던 어노테이션을 재활용하여 사용할 수 있도록 설정합니다
    - eg. `@PostMapping`, `@GetMapping`

### Request.Options

- Request.Options를 통해 Connection Timeout Time, Read Timeout Time을 설정할 수 있습니다.
- API 요청은 동기적이므로 Latency를 위하여 많은 시간을 소요하지 않도록 설정합니다.

### ErrorDecoder

```java
@Slf4j
public class UserClientErrorDecoder implements ErrorDecoder {

    private static final Set<Integer> RETRY_STATUS =
            Set.of(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.TOO_MANY_REQUESTS.value());

    @Override
    public Exception decode(String methodKey, Response response) {
        int status = response.status();
        String message = response.reason();
        if (status == HttpStatus.NOT_FOUND.value()) {
            return new UserNotFoundException();
        }
        if (isRetryStatus(status)) {
            return new RetryableException(
                    status, message, response.request().httpMethod(), null, response.request());
        }
        return new FeignException.FeignClientException(
                response.status(),
                message,
                response.request(),
                response.request().body(),
                response.request().headers());
    }

    private boolean isRetryStatus(int status) {
        return RETRY_STATUS.contains(status);
    }
}
```

- FeignClient는 커스텀 ErrorDecoder를 통해 에러를 핸들링하도록 지원합니다.
- API Response Status, Response Body에 따른 분기 처리를 위해 작성합니다.
- ErrorDecoder는 IOException을 핸들링하지 않습니다. (커넥션 관련 에러)
- RetryableException이 발생할 경우 정상응답이 올 때까지 Retryer에서 설장한 최대 회수 만큼 Retry하며 최대 회수동안 정상응답이 오지 않을 경우 feign.RetryableException을 발생합니다.

### Retryer

- Default: `Retryer.*NEVER_RETRY*`
- Default Retryer 는 100ms 를 시작으로 1.5 곱하면서 재시도를 하고, 최대 5번 합니다.
- ConnectException, SocketTimeoutException이 발생할 때 RetryableException 이 발생됩니다.
- http status code 에 따라서, Retry를 하고 싶은경우 별도의 ErrorDecode 를 만들어 거기서 httpStatus code 에 따라서 `RetryableException` 을 throw 해주면 됩니다.

### Logger.Level

- Default: Sl4fLogger
- Logger.Level을 적용하여 FeignClient 통신 시 로그를 확인 가능

## Reference

- [https://docs.spring.io/spring-cloud-openfeign/docs/current/reference/html/#spring-cloud-feign](https://docs.spring.io/spring-cloud-openfeign/docs/current/reference/html/#spring-cloud-feign)