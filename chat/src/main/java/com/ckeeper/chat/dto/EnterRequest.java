package com.ckeeper.chat.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EnterRequest {
    String roomId;
    String enterer;
}