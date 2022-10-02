package com.guide.domain.account.api;

import com.guide.domain.account.application.AccountCreateService;
import com.guide.domain.account.dto.AccountCreateRequest;
import com.guide.domain.account.dto.AccountResponse;
import com.guide.domain.account.entity.Account;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/account")
@RequiredArgsConstructor
@Tag(name = "Account", description = "Rest API for Account")
@ApiResponses(
        value = {
            @ApiResponse(responseCode = "400", description = "Account not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
        })
public class AccountController {

    private final AccountCreateService accountCreateService;

    @Operation(summary = "Create account")
    @ApiResponse(responseCode = "200", description = "OK")
    @PostMapping
    public AccountResponse create(@RequestBody @Valid AccountCreateRequest createRequest) {
        Account account = accountCreateService.create(createRequest);
        return AccountResponse.of(account);
    }
}
