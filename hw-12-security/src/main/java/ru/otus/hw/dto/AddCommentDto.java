package ru.otus.hw.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class AddCommentDto {

    @NotEmpty(message = "Comment text should not be empty")
    private String text;
}
