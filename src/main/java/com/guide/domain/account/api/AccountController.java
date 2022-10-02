package com.guide.domain.account.api;

import com.guide.domain.account.application.AccountCreateService;
import com.guide.domain.account.dto.AccountCreateRequest;
import com.guide.domain.account.dto.AccountResponse;
import com.guide.domain.account.entity.Account;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
