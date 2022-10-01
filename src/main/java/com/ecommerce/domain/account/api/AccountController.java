package com.ecommerce.domain.account.api;

import com.ecommerce.domain.account.application.AccountCreateService;
import com.ecommerce.domain.account.dto.AccountCreateRequest;
import com.ecommerce.domain.account.dto.AccountResponse;
import com.ecommerce.domain.account.entity.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountCreateService accountCreateService;

    @PostMapping
    public AccountResponse create(@RequestBody @Valid AccountCreateRequest createRequest) {
        Account account = accountCreateService.create(createRequest);
        return AccountResponse.of(account);
    }
}
