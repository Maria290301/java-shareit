package ru.practicum.shareit.exception;

public class NotOwnerAndNotBookerException extends RuntimeException {
    public NotOwnerAndNotBookerException(String message) {
        super(message);
    }
}
