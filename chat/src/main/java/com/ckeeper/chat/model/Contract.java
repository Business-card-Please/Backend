package com.ckeeper.chat.model;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class Contract {
    private boolean isContract;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String item;
}
