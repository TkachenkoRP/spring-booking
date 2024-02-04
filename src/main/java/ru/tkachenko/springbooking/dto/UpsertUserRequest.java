package ru.tkachenko.springbooking.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpsertUserRequest {
    @NotBlank(message = "Укажите имя пользователя!")
    private String name;
    @NotBlank(message = "Укажите пароль пользователя!")
    private String password;
    @NotBlank(message = "Укажите электронную почту пользователя!")
    private String email;
}
