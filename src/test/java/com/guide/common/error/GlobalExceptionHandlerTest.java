package com.guide.common.error;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

    private static final String ERROR_CODE = "errorCode";
    private static final String ERROR_MESSAGE = "errorMessage";
    private static final String REQUEST_URL = "/errors";

    @Test
    void methodArgumentNotValidException_발생_테스트() throws Exception {
        // GIVEN
        // WHEN
        final ResultActions resultActions = requestMethodArgumentNotValidException();

        // THEN
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(ERROR_CODE).value(ErrorCode.BAD_REQUEST.getCode()))
                .andExpect(jsonPath(ERROR_MESSAGE).value(ErrorCode.BAD_REQUEST.getMessage()));
    }

    @Test
    void httpRequestMethodNotSupportedException_발생_테스트() throws Exception {
        // GIVEN
        // WHEN
        final ResultActions resultActions = requestHttpRequestMethodNotSupportedException();

        // THEN
        resultActions
                .andExpect(status().isMethodNotAllowed())
                .andExpect(jsonPath(ERROR_CODE).value(ErrorCode.METHOD_NOT_ALLOWED.getCode()))
                .andExpect(jsonPath(ERROR_MESSAGE).value(ErrorCode.METHOD_NOT_ALLOWED.getMessage()));
    }

    @Test
    void noHandlerFoundException_발생_테스트() throws Exception {
        // GIVEN
        // WHEN
        final ResultActions resultActions = requestNoHandlerFoundException();

        // THEN
        resultActions
                .andExpect(status().isNotFound())
                .andExpect(jsonPath(ERROR_CODE).value(ErrorCode.NOT_HANDLER_FOUND.getCode()))
                .andExpect(jsonPath(ERROR_MESSAGE).value(ErrorCode.NOT_HANDLER_FOUND.getMessage()));
    }

    @Test
    void unCaughtException_발생_테스트() throws Exception {
        // GIVEN
        // WHEN
        ResultActions resultActions = requestUncaughtException();

        // THEN
        resultActions
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath(ERROR_CODE).value(ErrorCode.UNKNOWN.getCode()))
                .andExpect(jsonPath(ERROR_MESSAGE).value(ErrorCode.UNKNOWN.getMessage()));
    }

    private ResultActions requestMethodArgumentNotValidException() throws Exception {
        return mockMvc
                .perform(
                        post(REQUEST_URL)
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(
                                        objectMapper.writeValueAsString(
                                                new MethodArgumentNotValidExceptionRequest(" "))))
                .andDo(print());
    }

    private ResultActions requestHttpRequestMethodNotSupportedException() throws Exception {
        return mockMvc.perform(get(REQUEST_URL)).andDo(print());
    }

    private ResultActions requestNoHandlerFoundException() throws Exception {
        return mockMvc.perform(get("/not-registered-path")).andDo(print());
    }

    private ResultActions requestUncaughtException() throws Exception {
        return mockMvc
                .perform(
                        post(REQUEST_URL)
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(
                                        objectMapper.writeValueAsString(
                                                new MethodArgumentNotValidExceptionRequest("message"))))
                .andDo(print());
    }
}
