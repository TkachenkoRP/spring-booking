package ru.tkachenko.springbooking.exception;

public class BookingDateException extends RuntimeException {
    public BookingDateException(String message) {
        super(message);
    }
}
