package ru.practicum.ewm.exceptions;

public class AccessRejectException extends RuntimeException {
    public AccessRejectException(String msg) {
        super(msg);
    }
}
