package com.ckeeper.account.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "account")
public class AccountEntity {

    @Id
    @Column(name = "nickname", nullable = false, length = 16)
    private String nickname;

    @Column(name = "email", nullable = false, length = 255, unique = true)
    private String email;

    @Column(name = "college_name", nullable = false, length = 20)
    private String collegeName;

    @Column(name = "password", nullable = false, length = 60)
    private String password;
}