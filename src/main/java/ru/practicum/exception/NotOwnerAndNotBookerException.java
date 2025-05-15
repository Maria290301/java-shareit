package ru.practicum.exception;

public class NotOwnerAndNotBookerException extends RuntimeException {
    public NotOwnerAndNotBookerException(String message) {
        super(message);
    }
}
