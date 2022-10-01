package com.guide.domain.account.entity;

import com.guide.domain.model.User;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import lombok.Getter;

@Entity
@Getter
public class Account {

    @Id @GeneratedValue private Long id;

    private Long balance = 0L;

    private String name;

    private User user;

    public static Account of(String name, User user) {
        Account account = new Account();
        account.name = name;
        account.user = user;
        return account;
    }
}
