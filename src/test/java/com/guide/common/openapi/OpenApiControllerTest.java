package com.guide.common.openapi;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.guide.test.IntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultActions;

class OpenApiControllerTest extends IntegrationTest {

    private static final String GET_OPENAPI_URL = "/docs/openapi.yaml";

    @Test
    void should_return_200_when_method_called() throws Exception {
        // GIVEN
        // WHEN
        final ResultActions resultActions = requestOpenapi();
        // THEN
        resultActions.andExpect(status().isOk());
    }

    private ResultActions requestOpenapi() throws Exception {
        return mockMvc.perform(get(GET_OPENAPI_URL)).andDo(print());
    }
}
