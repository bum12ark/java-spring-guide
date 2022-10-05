package com.guide.common.error;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Profile("test")
public class ErrorGenerationController {

    @PostMapping("/errors")
    public ResponseEntity generateMethodArgumentNotValidException(
            @RequestBody @Valid MethodArgumentNotValidExceptionRequest request) {
        throw new RuntimeException("Unknown Error");
    }

    public record MethodArgumentNotValidExceptionRequest(@NotBlank String message) {}
}
