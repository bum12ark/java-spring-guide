package com.guide.common.healthcheck;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class HealthCheckControllerTest {

    @Autowired private MockMvc mockMvc;

    @Test
    @DisplayName("버전 정보를 반환하여야 합니다.")
    void should_return_version() throws Exception {
        this.mockMvc
                .perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("version").isNotEmpty());
    }
}
