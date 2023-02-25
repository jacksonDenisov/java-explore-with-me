package ru.practicum.ewm.service.category;

import org.springframework.data.domain.Pageable;
import ru.practicum.ewm.model.category.CategoryDtoFull;
import ru.practicum.ewm.model.category.CategoryDtoNew;

import java.util.List;

public interface CategoryService {

    CategoryDtoFull create(CategoryDtoNew categoryDtoNew);

    void delete(Long id);

    CategoryDtoFull update(CategoryDtoNew categoryDtoNew, Long id);

    List<CategoryDtoFull> findAll(Pageable pageable);

    CategoryDtoFull findById(Long id);
}
