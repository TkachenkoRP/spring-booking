package ru.tkachenko.springbooking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
}
