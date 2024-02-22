package ru.tkachenko.springbooking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomResponse {
    private Long id;
    private String name;
    private String description;
    private int number;
    private double price;
    private Byte capacity;
    private Instant createAt;
    private Instant updatedAt;
    private String hotelName;
    private Long hotelId;
}
