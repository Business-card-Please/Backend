package com.ckeeper.rentalboard.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class RentalBoardSelectRequest {
    LocalDateTime datetime;
    Integer size;
    String type;
    String data;
    String department1;
    String department2;
}
