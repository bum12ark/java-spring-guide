package com.guide.domain.account.dto;

import com.guide.domain.account.entity.Account;
import com.guide.domain.model.User;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public record AccountCreateRequest(
        @NotNull Long userId, @NotBlank String email, @NotBlank String name) {

    public Account toEntity() {
        User user = User.builder().id(userId).email(email).build();
        return Account.of(name, user);
    }
}
