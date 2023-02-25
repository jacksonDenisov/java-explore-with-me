package ru.practicum.ewm.service.category;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.exceptions.DataBaseConflictException;
import ru.practicum.ewm.exceptions.NotFoundException;
import ru.practicum.ewm.model.category.Category;
import ru.practicum.ewm.model.category.CategoryDtoFull;
import ru.practicum.ewm.model.category.CategoryDtoNew;
import ru.practicum.ewm.model.category.CategoryMapper;
import ru.practicum.ewm.storage.category.CategoryRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    @Transactional
    public CategoryDtoFull create(CategoryDtoNew categoryDtoNew) {
        try {
            Category category = categoryRepository.save(CategoryMapper.toCategory(categoryDtoNew));
            return CategoryMapper.toCategoryDtoFull(category);
        } catch (DataIntegrityViolationException e) {
            throw new DataBaseConflictException("Не удалось добавить новую категорию",
                    "Нарушение целостности данных",
                    new ArrayList<>(Collections.singletonList(e.getMessage())));
        }
    }

    @Override
    @Transactional
    public void delete(Long id) {
        try {
            categoryRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Не удалось удалить категорию",
                    "Такого категории в системе не существует",
                    new ArrayList<>(Collections.singletonList(e.getMessage())));
        }
    }

    @Override
    @Transactional
    public CategoryDtoFull update(CategoryDtoNew categoryDtoNew, Long id) {
        try {
            Category category = categoryRepository.findById(id).get();
            if (!category.getName().equals(categoryDtoNew.getName())) {
                category.setName(categoryDtoNew.getName());
                Category categoryChanged = categoryRepository.save(category);
                return CategoryMapper.toCategoryDtoFull(categoryChanged);
            } else {
                throw new DataBaseConflictException("Не удалось обновить новую категорию",
                        "Нарушение целостности данных - указанное имя категории уже задействовано",
                        new ArrayList<>(Collections.singletonList("DataBaseConflictException")));
            }
        } catch (NoSuchElementException e) {
            throw new NotFoundException("Не удалось найти категорию",
                    "Вероятно, указан несуществующий id",
                    new ArrayList<>(Collections.singletonList(e.getMessage())));
        }
    }

    @Override
    @Transactional
    public List<CategoryDtoFull> findAll(Pageable pageable) {
        Page<Category> categoryPage = categoryRepository.findAll(pageable);
        return CategoryMapper.toCategoryDtoFull(categoryPage.toList());
    }

    @Override
    @Transactional
    public CategoryDtoFull findById(Long id) {
        try {
            Category category = categoryRepository.findById(id).get();
            return CategoryMapper.toCategoryDtoFull(category);
        } catch (NoSuchElementException e) {
            throw new NotFoundException("Не удалось найти категорию",
                    "Категории с запрошенным id в системе не существует",
                    new ArrayList<>(Collections.singletonList(e.getMessage())));
        }
    }
}
