package com.ckeeper.account.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RegistrationRequest {
    private String email;
    private String collegeName;
    private String password;
    private String name;
    private String nickname;
    private Short grade;
    private String department1;
    private String department2;
    private List<String> keywords;
}
