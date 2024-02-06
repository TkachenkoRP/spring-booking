package ru.tkachenko.springbooking.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpsertBookingRequest {
    @NotNull(message = "Укажите дату заезда!")
    @Future(message = "Указана неверная дата заезда!")
    private LocalDate arrivalDate;
    @NotNull(message = "Укажите дату выезда!")
    @Future(message = "Указана неверная дата выезда!")
    private LocalDate departureDate;
    @NotNull(message = "Укажите ID комнаты!")
    private Long roomId;
    @NotNull(message = "Укажите ID Пользователя!")
    private Long userId;
}
