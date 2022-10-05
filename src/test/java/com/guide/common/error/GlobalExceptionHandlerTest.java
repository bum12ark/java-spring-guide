package com.guide.common.error;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.guide.common.error.ErrorGenerationController.MethodArgumentNotValidExceptionRequest;
import com.guide.test.IntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

class GlobalExceptionHandlerTest extends IntegrationTest {

    @Test
    void MethodArgumentNotValidException_발생_테스트() throws Exception {
        // GIVEN
        // WHEN
        final ResultActions resultActions = requestMethodArgumentNotValidException();

        // THEN
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("errorCode").value(ErrorCode.BAD_REQUEST.getCode()))
                .andExpect(jsonPath("errorMessage").value(ErrorCode.BAD_REQUEST.getMessage()));
    }

    private ResultActions requestMethodArgumentNotValidException() throws Exception {
        return mockMvc
                .perform(
                        post("/error")
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(
                                        objectMapper.writeValueAsString(
                                                new MethodArgumentNotValidExceptionRequest(" "))))
                .andDo(print());
    }
}
