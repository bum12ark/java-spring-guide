package com.guide.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.guide.Application;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class IntegrationTest {

    @Autowired protected MockMvc mockMvc;
    protected ObjectMapper objectMapper = new ObjectMapper();
}
