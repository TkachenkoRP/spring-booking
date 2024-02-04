package ru.tkachenko.springbooking.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpsertRoomRequest {
    @NotBlank(message = "Укажите название комнаты!")
    private String name;
    @NotBlank(message = "Укажите описание комнаты!")
    private String description;
    @NotNull(message = "Укажите номер комнаты!")
    @Positive(message = "Номер комнаты должен быть больше 0!")
    @Max(value = 100, message = "Номер комнаты должен быть меньше {value}!")
    private Integer number;
    @Positive(message = "Укажите стоимость комнаты!")
    private double price;
    @Min(value = 1, message = "Количество мест в комнате должно быть больше {value}!")
    private byte capacity;
    @NotNull(message = "Укажите номер отеля!")
    private Long hotelId;
}
