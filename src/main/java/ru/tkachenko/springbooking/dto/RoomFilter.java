package ru.tkachenko.springbooking.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RoomFilter {
    private Long id;
    private String name;
    private Double minPrice;
    private Double maxPrice;
    private Integer countGuest;
    private String arrivalDate;
    private String departureDate;
    private Long hotelId;
    private Integer pageSize = 20;
    private Integer pageNumber = 0;
}
