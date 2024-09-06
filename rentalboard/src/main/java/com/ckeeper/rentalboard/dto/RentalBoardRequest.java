package com.ckeeper.rentalboard.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class RentalBoardRequest {
    private String idx;
    private String title;
    private String lecture;
    private String department;
    private String content;
}
