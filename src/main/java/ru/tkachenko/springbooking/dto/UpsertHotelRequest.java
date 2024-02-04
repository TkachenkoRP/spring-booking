package ru.tkachenko.springbooking.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpsertHotelRequest {
    @NotBlank(message = "Укажите название отеля!")
    private String name;
    @NotBlank(message = "Укажите заголовок объявления!")
    private String title;
    @NotBlank(message = "Укажите город!")
    private String city;
    @NotBlank(message = "Укажите адрес отеля!")
    private String address;
    @Positive(message = "Расстояние от центра должно быть указано и быть больше 0!")
    private double distanceFromCityCenter;
}
