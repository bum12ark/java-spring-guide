package com.guide.common.healthcheck;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.guide.test.IntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HealthCheckControllerTest extends IntegrationTest {

    @Test
    @DisplayName("버전 정보를 반환하여야 합니다.")
    void should_return_version() throws Exception {
        this.mockMvc
                .perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("version").isNotEmpty());
    }
}
