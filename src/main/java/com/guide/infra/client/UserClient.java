package com.guide.infra.client;

import com.guide.infra.client.config.UserClientConfiguration;
import com.guide.infra.client.dto.UserFindApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "user-api",
        url = "${user.feign.base-url}",
        configuration = UserClientConfiguration.class)
public interface UserClient {

    @GetMapping("/api/users/{userId}")
    UserFindApiResponse requestUserById(@PathVariable("userId") long userId);
}
