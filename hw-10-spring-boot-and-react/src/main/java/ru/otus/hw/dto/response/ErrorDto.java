package ru.otus.hw.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ErrorDto {

    private String message;

    private String code;

    private LocalDateTime localDateTime;

    public ErrorDto(String message, String code) {
        this.message = message;
        this.code = code;
        this.localDateTime = LocalDateTime.now();
    }
}
