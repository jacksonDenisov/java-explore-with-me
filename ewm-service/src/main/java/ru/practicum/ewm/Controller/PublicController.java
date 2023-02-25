package ru.practicum.ewm.Controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.model.category.CategoryDtoFull;
import ru.practicum.ewm.service.category.CategoryService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@Validated
public class PublicController {

    private final CategoryService categoryService;

    @GetMapping("/categories")
    protected List<CategoryDtoFull> findAllCategories(@RequestParam(required = false, defaultValue = "0")
                                                      @PositiveOrZero int from,
                                                      @RequestParam(required = false, defaultValue = "10")
                                                      @PositiveOrZero int size) {
        log.info("Получен запрос на получение категорий");
        Pageable pageable = PageRequest.of(from / size, size);
        return categoryService.findAll(pageable);
    }

    @GetMapping("/categories/{catId}")
    protected CategoryDtoFull findCategoryById(@PathVariable @Positive Long catId) {
        log.info("Получен запрос на получение категории с id {}", catId);
        return categoryService.findById(catId);
    }
}
