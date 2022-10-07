package com.guide.infra.client;

import feign.FeignException;
import feign.Response;
import feign.RetryableException;
import feign.codec.ErrorDecoder;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

@Slf4j
public class UserClientErrorDecoder implements ErrorDecoder {

    private static final Set<Integer> RETRY_STATUS =
            Set.of(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    HttpStatus.TOO_MANY_REQUESTS.value());

    @Override
    public Exception decode(String methodKey, Response response) {
        int status = response.status();
        if (isRetryStatus(status)) {
            return new RetryableException(
                    status, "message", response.request().httpMethod(), null, response.request());
        }
        return new FeignException.FeignClientException(
                response.status(),
                "message",
                response.request(),
                response.request().body(),
                response.request().headers());
    }

    private boolean isRetryStatus(int status) {
        return RETRY_STATUS.contains(status);
    }
}
