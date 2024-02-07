package ru.tkachenko.springbooking.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class HotelFilter {
    private Long id;
    private String name;
    private String title;
    private String city;
    private String address;
    private Double distance;
    private Double rating;
    private Integer numberOfRatings;
}
