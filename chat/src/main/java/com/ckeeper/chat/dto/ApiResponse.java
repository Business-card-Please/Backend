package com.ckeeper.chat.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiResponse {
    private Boolean status;
    private Object data;

    public ApiResponse(Boolean status, Object data) {
        this.status = status;
        this.data = data;
    }
}
