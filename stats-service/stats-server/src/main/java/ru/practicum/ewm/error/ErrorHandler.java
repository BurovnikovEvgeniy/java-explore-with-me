package ru.practicum.ewm.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.ewm.error.exceptions.ValidationException;

import java.time.LocalDateTime;
import java.util.Arrays;

import static ru.practicum.ewm.util.Constants.FORMATTER;


@Slf4j
@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleValidationException(ValidationException e) {
        log.info("Incorrect request {}", e.getMessage());
        return new ApiError(e.getMessage(), "Incorrect request", HttpStatus.BAD_REQUEST.name(),
                LocalDateTime.now().format(FORMATTER));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError handleServerException(RuntimeException e) {
        log.info("Incorrect request {}", e.getMessage());
        return new ApiError(e.getMessage(), Arrays.toString(e.getStackTrace()), HttpStatus.INTERNAL_SERVER_ERROR.name(),
                LocalDateTime.now().format(FORMATTER));
    }
}