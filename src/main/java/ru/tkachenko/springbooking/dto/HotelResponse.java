package ru.tkachenko.springbooking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HotelResponse {
    private Long id;
    private String name;
    private String title;
    private String city;
    private String address;
    private double distanceFromCityCenter;
    private double rating;
    private int numberOfRatings;
    private Instant createAt;
    private Instant updatedAt;
}
