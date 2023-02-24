package ru.practicum.ewm.Controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.model.user.UserDtoFull;
import ru.practicum.ewm.model.user.UserDtoNew;
import ru.practicum.ewm.service.user.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@Slf4j
@Validated
public class AdminController {

    private final UserService userService;


    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    protected UserDtoFull createUser(@RequestBody @Valid UserDtoNew userDtoNew) {
        log.info("Получен запрос на добавление пользователя {}", userDtoNew);
        return userService.create(userDtoNew);
    }

    @DeleteMapping("/users/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    protected void deleteUser(@PathVariable Long userId) {
        log.info("Получен запрос на удаление пользователя с id {}", userId);
        userService.delete(userId);
    }

    @GetMapping("/users")
    protected List<UserDtoFull> findUsers(@RequestParam List<Long> ids,
                                          @RequestParam(name = "from", required = false, defaultValue = "0") int from,
                                          @RequestParam(name = "size", required = false, defaultValue = "20") int size) {
        log.info("Получен запрос на поиск пользователей с id: {}. from = {}, size = {}", ids, from, size);
        Pageable pageable = PageRequest.of(from / size, size);
        return userService.findUsers(ids, pageable);
    }
}
