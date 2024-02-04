package ru.tkachenko.springbooking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HotelListResponse {
    private List<HotelResponse> hotels;
    private int pageSize;
    private int pageNumber;
}
