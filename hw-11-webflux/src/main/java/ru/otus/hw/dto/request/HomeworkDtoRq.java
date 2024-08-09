package ru.otus.hw.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HomeworkDtoRq {

    private String id;

    @NotEmpty(message = "Homework text should not be empty")
    private String text;

    @NotNull(message = "Homework should refer to a student")
    private String studentId;
}
