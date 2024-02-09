package ru.tkachenko.springbooking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomBookedEvent implements Event {
    private Long userId;
    private String checkInDate;
    private String checkOutDate;
}
