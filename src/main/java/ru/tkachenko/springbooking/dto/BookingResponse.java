package ru.tkachenko.springbooking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingResponse {
    private Long id;
    private LocalDate arrivalDate;
    private LocalDate departureDate;
    private RoomResponse room;
    private UserResponse user;
}
