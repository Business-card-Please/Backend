package com.ckeeper.account.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RegistrationRequest3 {
    private String email;
    private List<String> keyword;
}
