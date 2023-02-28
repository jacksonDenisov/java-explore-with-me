package ru.practicum.ewm.model.user;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class UserDtoNew {

    @NotBlank(message = "Имя пользователя должно быть заполнено")
    private String name;

    @NotBlank(message = "Email пользователя должен быть заполнен")
    @Email(message = "Должен быть указан корректный email")
    private String email;
}
