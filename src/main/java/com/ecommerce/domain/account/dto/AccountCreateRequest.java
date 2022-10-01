package com.ecommerce.domain.account.dto;

import com.ecommerce.domain.account.entity.Account;
import com.ecommerce.domain.model.User;

import javax.validation.constraints.NotNull;

public record AccountCreateRequest(
   @NotNull Long userId,
   @NotNull String email,
   @NotNull String name
) {
    public Account toEntity() {
        User user = new User(userId, email);
        return Account.of(name, user);
    }
}
