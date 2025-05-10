package ru.practicum.exception;

public class IncorrectBookingTimeException extends RuntimeException {
    public IncorrectBookingTimeException(String message) {
        super(message);
    }
}
