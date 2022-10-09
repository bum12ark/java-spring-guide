package com.guide.infra.client.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.guide.infra.client.UserClientErrorDecoder;
import feign.Contract;
import feign.Logger;
import feign.Logger.Level;
import feign.Request;
import feign.Request.Options;
import feign.Retryer;
import feign.Retryer.Default;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.codec.ErrorDecoder;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.cloud.openfeign.support.SpringMvcContract;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

@Configuration
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
