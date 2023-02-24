package ru.practicum.ewm.model.category;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CategoryMapper {

    public static Category toCategory(CategoryDtoNew categoryDtoNew) {
        Category category = new Category();
        category.setName(categoryDtoNew.getName());
        return category;
    }

    public static CategoryDtoFull toCategoryDtoFull(Category category) {
        CategoryDtoFull categoryDtoFull = new CategoryDtoFull();
        categoryDtoFull.setId(category.getId());
        categoryDtoFull.setName(category.getName());
        return categoryDtoFull;
    }
}
