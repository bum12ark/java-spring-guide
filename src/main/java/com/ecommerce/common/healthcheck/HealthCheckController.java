package com.ecommerce.common.healthcheck;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.info.BuildProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class HealthCheckController {

    private final BuildProperties buildProperties;

    @GetMapping("/")
    public SystemStatus getSystemStatus() {
        final String version = buildProperties.getVersion();
        return new SystemStatus(version);
    }
}
