package ru.otus.hw.exceptions;

public class CorrespondingIdNotFoundException extends RuntimeException {
    public CorrespondingIdNotFoundException(String message) {
        super(message);
    }
}
