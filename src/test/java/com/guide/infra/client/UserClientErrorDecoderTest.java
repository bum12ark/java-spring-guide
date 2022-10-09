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
import org.junit.jupiter.api.Test;

class UserClientErrorDecoderTest {

    private final Map<String, Collection<String>> headers = new LinkedHashMap<>();
    private final ErrorDecoder errorDecoder = new UserClientErrorDecoder();

    @Test
    void throwsFeignException() {
        // GIVEN
        Response response = createFeignResponse(502, "Bad Gateway Exception");

        // WHEN & THEN
        assertThrows(
                FeignException.class,
                () -> {
                    throw errorDecoder.decode("", response);
                });
    }

    @Test
    void throwsRetryException() {
        // GIVEN
        Response response = createFeignResponse(500, "Internal Server Error");

        // WHEN & THEN
        assertThrows(
                RetryableException.class,
                () -> {
                    throw errorDecoder.decode("", response);
                });
    }

    private Response createFeignResponse(int status, String reason) {
        return Response.builder()
                .status(status)
                .reason(reason)
                .request(
                        Request.create(HttpMethod.GET, "/api", Collections.emptyMap(), null, Util.UTF_8, null))
                .headers(headers)
                .build();
    }
}
