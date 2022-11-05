package com.guide.common.openapi;

import java.io.IOException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.yaml.snakeyaml.Yaml;

@RestController
public class OpenApiController {

    private static final String OPEN_API_FILE = "openapi.yaml";

    @GetMapping(value = "/docs/openapi.yaml", produces = MediaType.TEXT_PLAIN_VALUE)
    public String getOpenApi() throws IOException {
        ClassPathResource classPathResource = new ClassPathResource(OPEN_API_FILE);
        Yaml yaml = new Yaml();
        Object openApi = yaml.load(classPathResource.getInputStream());
        return yaml.dump(openApi);
    }
}
