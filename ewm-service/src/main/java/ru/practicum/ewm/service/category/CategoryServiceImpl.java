package ru.practicum.ewm.service.category;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
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

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    @Transactional
    public CategoryDtoFull create(CategoryDtoNew categoryDtoNew) {
        try {
            return CategoryMapper.toCategoryDtoFull(
                    categoryRepository.save(CategoryMapper.toCategory(categoryDtoNew)));
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
                    "Такой категории в системе не существует",
                    new ArrayList<>(Collections.singletonList(e.getMessage())));
        }
    }

    @Override
    @Transactional
    public CategoryDtoFull update(CategoryDtoNew categoryDtoNew, Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new DataBaseConflictException("Не удалось обновить категорию",
                        "Нарушение целостности данных - указанное имя категории уже задействовано",
                        new ArrayList<>(Collections.singletonList("DataBaseConflictException"))));
        if (category.getName().equals(categoryDtoNew.getName())) {
            throw new DataBaseConflictException("Не удалось обновить категорию",
                    "Нарушение целостности данных - указанное имя категории уже задействовано",
                    new ArrayList<>(Collections.singletonList("DataBaseConflictException")));
        }
        category.setName(categoryDtoNew.getName());
        return CategoryMapper.toCategoryDtoFull(categoryRepository.save(category));
    }

    @Override
    @Transactional
    public List<CategoryDtoFull> findAll(Pageable pageable) {
        return CategoryMapper.toCategoryDtoFull(categoryRepository.findAll(pageable).toList());
    }

    @Override
    @Transactional
    public CategoryDtoFull findById(Long id) {
        return CategoryMapper.toCategoryDtoFull(categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Не удалось найти категорию",
                        "Категории с запрошенным id в системе не существует",
                        new ArrayList<>(Collections.singletonList("NotFoundException")))));
    }
}
