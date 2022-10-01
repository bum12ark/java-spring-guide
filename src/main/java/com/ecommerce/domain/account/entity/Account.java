package com.ecommerce.domain.account.entity;

import com.ecommerce.domain.model.User;
import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

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
