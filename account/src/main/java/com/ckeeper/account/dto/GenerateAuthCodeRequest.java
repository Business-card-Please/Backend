package com.ckeeper.account.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GenerateAuthCodeRequest {
    private String email;
}
