package com.guide.domain.account.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public record AccountCreateRequest(@NotNull Long userId, @NotBlank String name) {}
