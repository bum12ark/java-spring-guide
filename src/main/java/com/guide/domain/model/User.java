package com.guide.domain.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotEmpty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString(of = {"id", "email"})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
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
