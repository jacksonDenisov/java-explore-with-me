package ru.practicum.ewm.exceptions;

import lombok.Getter;

import java.util.List;

@Getter
public class NotFoundException extends RuntimeException {

    private final String reason;
    private final List<String> errors;

    public NotFoundException(final String message, final String reason, final List<String> errors) {
        super(message);
        this.reason = reason;
        this.errors = errors;
    }
}