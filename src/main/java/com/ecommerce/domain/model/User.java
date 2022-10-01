package com.ecommerce.domain.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotEmpty;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class User {

    @NotEmpty
    @Column(name = "user_id")
    private Long id;

    @NotEmpty
    @Column(name = "email")
    private String email;

    @Builder
    public User(final Long id, final String email) {
        this.id = id;
        this.email = email;
    }
}
