package ru.practicum.ewm.service.category;

import ru.practicum.ewm.model.category.CategoryDtoFull;
import ru.practicum.ewm.model.category.CategoryDtoNew;

public interface CategoryService {

    CategoryDtoFull create(CategoryDtoNew categoryDtoNew);

    void delete(Long id);

    CategoryDtoFull update(CategoryDtoNew categoryDtoNew, Long id);
}
