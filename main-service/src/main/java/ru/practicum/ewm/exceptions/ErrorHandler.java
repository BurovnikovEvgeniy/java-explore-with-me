package ru.practicum.ewm.exceptions;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

@RestControllerAdvice
public class ErrorHandler {
    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleValidationException(NoValidDataParams e) {
        return new ApiError(HttpStatus.BAD_REQUEST.name(), "Incorrect request", e.getMessage(),
                LocalDateTime.now().format(FORMATTER));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ApiError handleForbiddenException(AccessRejectException e) {
        return new ApiError(HttpStatus.FORBIDDEN.name(), "Incorrectly conditions fot this request",
                e.getMessage(), LocalDateTime.now().format(FORMATTER));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleNotFoundException(EntityNotFoundException e) {
        return new ApiError(HttpStatus.NOT_FOUND.name(), "The required object was not found", e.getMessage(),
                LocalDateTime.now().format(FORMATTER));
    }

    @ExceptionHandler({DataAccessException.class, ConflictException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleConflictException(RuntimeException e) {
        return new ApiError(HttpStatus.CONFLICT.name(), "Incorrectly conditions fot this request",
                e.getMessage(), LocalDateTime.now().format(FORMATTER));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError handleServerException(RuntimeException e) {
        return new ApiError(HttpStatus.INTERNAL_SERVER_ERROR.name(), e.getMessage(), Arrays.toString(e.getStackTrace()),
                LocalDateTime.now().format(FORMATTER));
    }
}