package ru.tkachenko.springbooking.exception;

public class CreateFolderException extends RuntimeException {
    public CreateFolderException(String message) {
        super(message);
    }
}
