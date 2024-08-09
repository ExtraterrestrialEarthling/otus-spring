package ru.otus.hw.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentDtoRq {

    private String id;

    @NotEmpty(message = "Name cannot be empty")
    private String name;

    @NotNull(message = "Student should have a teacher")
    private String teacherId;

    @NotEmpty(message = "At least one skill must be selected.")
    private Set<String> skillIds;
}
