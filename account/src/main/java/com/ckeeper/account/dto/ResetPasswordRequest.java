package com.ckeeper.account.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResetPasswordRequest {
    private String nickname;
    private String newPassword;
}
