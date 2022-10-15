package com.guide.domain.account.api;

import com.guide.domain.account.application.AccountCreateService;
import com.guide.domain.account.application.AccountFindService;
import com.guide.domain.account.dto.AccountCreateRequest;
import com.guide.domain.account.dto.AccountResponse;
import com.guide.domain.account.entity.Account;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Account", description = "Rest API for Account")
@ApiResponses(
        value = {
            @ApiResponse(responseCode = "400", description = "Parameter is not valid"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
        })
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/account")
@RestController
public class AccountController {

    private final AccountCreateService accountCreateService;
    private final AccountFindService accountFindService;

    @Operation(summary = "Create account")
    @ApiResponse(responseCode = "200", description = "OK")
    @ApiResponse(responseCode = "502", description = "User api server error")
    @PostMapping
    public AccountResponse create(@RequestBody @Valid AccountCreateRequest createRequest) {
        log.info("Requested for create Account, AccountCreateRequest : {}", createRequest);
        final Account account = accountCreateService.create(createRequest);
        final AccountResponse accountResponse = AccountResponse.of(account);
        log.info("Returned created Account, AccountResponse : {}", accountResponse);
        return accountResponse;
    }

    @Operation(summary = "Find account by account id")
    @ApiResponse(responseCode = "404", description = "Account not found")
    @GetMapping("/{accountId}")
    public AccountResponse findAccount(@PathVariable("accountId") long accountId) {
        log.info("Requested for find Account, accountId : {}", accountId);
        final Account account = accountFindService.findAccount(accountId);
        final AccountResponse accountResponse = AccountResponse.of(account);
        log.info("Return find Account, AccountResponse : {}", accountResponse);
        return accountResponse;
    }
}
