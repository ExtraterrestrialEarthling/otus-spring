package ru.otus.hw.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GenreDtoRq {

    private Long id;

    @NotEmpty(message = "Genre name should not be empty")
    private String name;
}
