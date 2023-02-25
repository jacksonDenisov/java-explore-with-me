package ru.practicum.ewm.Controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.model.category.CategoryDtoFull;
import ru.practicum.ewm.model.category.CategoryDtoNew;
import ru.practicum.ewm.model.user.UserDto;
import ru.practicum.ewm.model.user.UserDtoNew;
import ru.practicum.ewm.service.category.CategoryService;
import ru.practicum.ewm.service.user.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@Slf4j
@Validated
public class AdminController {

    private final UserService userService;
    private final CategoryService categoryService;


    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    protected UserDto createUser(@RequestBody @Valid UserDtoNew userDtoNew) {
        log.info("Получен запрос на добавление пользователя {}", userDtoNew);
        return userService.create(userDtoNew);
    }

    @DeleteMapping("/users/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    protected void deleteUser(@PathVariable @Positive Long userId) {
        log.info("Получен запрос на удаление пользователя с id {}", userId);
        userService.delete(userId);
    }

    @GetMapping("/users")
    protected List<UserDto> findUsers(@RequestParam List<Long> ids,
                                      @RequestParam(name = "from", required = false, defaultValue = "0")
                                      @PositiveOrZero int from,
                                      @RequestParam(name = "size", required = false, defaultValue = "10")
                                      @PositiveOrZero int size) {
        log.info("Получен запрос на поиск пользователей с id: {}. from = {}, size = {}", ids, from, size);
        Pageable pageable = PageRequest.of(from / size, size);
        return userService.findAllByIds(ids, pageable);
    }

    @PostMapping("/categories")
    @ResponseStatus(HttpStatus.CREATED)
    protected CategoryDtoFull createCategory(@RequestBody @Valid CategoryDtoNew categoryDtoNew) {
        log.info("Получен запрос на создание категории {}", categoryDtoNew);
        return categoryService.create(categoryDtoNew);
    }

    @DeleteMapping("/categories/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    protected void deleteCategory(@PathVariable @Positive Long catId) {
        log.info("Получен запрос на удаление категории с id {}", catId);
        categoryService.delete(catId);
    }

    @PatchMapping("/categories/{catId}")
    protected CategoryDtoFull update(@RequestBody @Valid CategoryDtoNew categoryDtoNew,
                                     @PathVariable @Positive Long catId) {
        log.info("Получен запрос на обновление категории с id {}", catId);
        return categoryService.update(categoryDtoNew, catId);
    }
}
