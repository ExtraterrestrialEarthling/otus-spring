package ru.haidarov.hw.exceptions;

public class InputReadException extends RuntimeException {
    public InputReadException(String message, Throwable cause) {
        super(message, cause);
    }

    public InputReadException(String message){
        super(message);
    }
}
