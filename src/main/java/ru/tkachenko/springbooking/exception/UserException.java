package ru.tkachenko.springbooking.exception;

public class UserException extends RuntimeException {
    public UserException(String message) {
        super(message);
    }
}
