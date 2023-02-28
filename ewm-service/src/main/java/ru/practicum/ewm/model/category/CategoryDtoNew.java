package ru.practicum.ewm.model.category;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CategoryDtoNew {

    @NotBlank(message = "Наименование категории должно быть заполнено")
    private String name;
}
