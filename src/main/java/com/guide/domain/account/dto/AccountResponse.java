package com.guide.domain.account.dto;

import com.guide.domain.account.entity.Account;
import com.guide.domain.model.User;
import com.fasterxml.jackson.annotation.JsonProperty;

public record AccountResponse(
        @JsonProperty("id") Long id,
        @JsonProperty("name") String name,
        @JsonProperty("balance") Long balance,
        @JsonProperty("user") User user) {

    public static AccountResponse of(Account account) {
        return new AccountResponse(
                account.getId(), account.getName(), account.getBalance(), account.getUser());
    }
}
