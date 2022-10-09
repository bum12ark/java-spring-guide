package com.guide.infra.client;

import static org.junit.jupiter.api.Assertions.*;

import feign.FeignException;
import feign.Request;
import feign.Request.HttpMethod;
import feign.Response;
import feign.RetryableException;
import feign.Util;
import feign.codec.ErrorDecoder;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.HttpStatus;

@TestInstance(Lifecycle.PER_CLASS)
class UserClientErrorDecoderTest {

    private static final Map<String, Collection<String>> HEADERS = new LinkedHashMap<>();
    private static final String METHOD_KEY = "Service#foo()";
    private final ErrorDecoder errorDecoder = new UserClientErrorDecoder();

    @Test
    void throwsFeignException() {
        // GIVEN
        Response response =
                createFeignResponse(HttpStatus.BAD_GATEWAY.value(), HttpStatus.BAD_GATEWAY.name());

        // WHEN & THEN
        assertThrows(
                FeignException.class,
                () -> {
                    throw errorDecoder.decode(METHOD_KEY, response);
                });
    }

    @ParameterizedTest
    @MethodSource(value = "createRetryStatusMethodSource")
    void throwsRetryableException(final Response retryResponse) {
        // GIVEN
        // WHEN & THEN
        assertThrows(
                RetryableException.class,
                () -> {
                    throw errorDecoder.decode(METHOD_KEY, retryResponse);
                });
    }

    Stream<Arguments> createRetryStatusMethodSource() {
        return Stream.of(
                Arguments.arguments(
                        createFeignResponse(
                                HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.name())),
                Arguments.arguments(
                        createFeignResponse(
                                HttpStatus.TOO_MANY_REQUESTS.value(), HttpStatus.TOO_MANY_REQUESTS.name())));
    }

    Response createFeignResponse(int status, String reason) {
        return Response.builder()
                .status(status)
                .reason(reason)
                .request(
                        Request.create(HttpMethod.GET, "/api", Collections.emptyMap(), null, Util.UTF_8, null))
                .headers(HEADERS)
                .build();
    }
}
