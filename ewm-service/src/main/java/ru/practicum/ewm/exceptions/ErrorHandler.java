package ru.practicum.ewm.exceptions;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import ru.practicum.ewm.controller.AdminController;
import ru.practicum.ewm.controller.PrivateController;
import ru.practicum.ewm.controller.PublicController;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.Collections;

@RestControllerAdvice(assignableTypes = {AdminController.class, PublicController.class, PrivateController.class})
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleEmptyResultDataAccessException(final NotFoundException e) {
        return new ApiError(e.getErrors(), e.getMessage(), e.getReason(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleBusinessLogicConflictException(final BusinessLogicConflictException e) {
        return new ApiError(e.getErrors(), e.getMessage(), e.getReason(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleDataIntegrityViolationException(final DataBaseConflictException e) {
        return new ApiError(e.getErrors(), e.getMessage(), e.getReason(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleMethodArgumentNotValidException(final MethodArgumentNotValidException e) {
        return new ApiError(new ArrayList<>(Collections.singletonList(e.getMessage())),
                "Не удалось обработать запрос",
                "Некорректное заполнение полей объекта",
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleMethodArgumentTypeMismatchException(final MethodArgumentTypeMismatchException e) {
        return new ApiError(new ArrayList<>(Collections.singletonList(e.getMessage())),
                "Не удалось обработать запрос",
                "Некорректное заполнение параметров запроса",
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleConstraintViolationException(final ConstraintViolationException e) {
        return new ApiError(new ArrayList<>(Collections.singletonList(e.getMessage())),
                "Не удалось обработать запрос",
                "Некорректное заполнение параметров запроса",
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleDataIntegrityViolationException(final DataIntegrityViolationException e) {
        return new ApiError(new ArrayList<>(Collections.singletonList(e.getMessage())),
                "Нарушение целостности данных",
                e.getCause().getCause().getMessage(),
                HttpStatus.CONFLICT);
    }
}