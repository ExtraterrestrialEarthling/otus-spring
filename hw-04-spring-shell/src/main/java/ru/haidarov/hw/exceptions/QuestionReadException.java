package ru.haidarov.hw.exceptions;

public class QuestionReadException extends RuntimeException {
    public QuestionReadException(String message, Throwable ex) {
        super(message, ex);
    }
}
