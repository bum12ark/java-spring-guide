package com.guide.common.healthcheck;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.info.BuildProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "Health Check", description = "Rest API for Server Health Check")
@ApiResponses(value = {@ApiResponse(responseCode = "500", description = "Internal server error")})
public class HealthCheckController {

    private final BuildProperties buildProperties;

    @Operation(summary = "Show server version")
    @ApiResponse(responseCode = "200", description = "OK")
    @GetMapping("/")
    public SystemStatus getSystemStatus() {
        final String version = buildProperties.getVersion();
        return new SystemStatus(version);
    }
}
