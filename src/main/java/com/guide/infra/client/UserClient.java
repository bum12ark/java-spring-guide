package com.guide.infra.client;

import com.guide.infra.client.config.UserClientConfiguration;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(
        name = "user-api",
        url = "${user.feign.base-url}",
        configuration = UserClientConfiguration.class)
public interface UserClient {}
