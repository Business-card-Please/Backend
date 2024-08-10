package com.ckeeper.account.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistrationRequest2 {
    private String email;
    private String name;
    private String nickname;
    private Short grade;
    private String department1;
    private String department2;
}