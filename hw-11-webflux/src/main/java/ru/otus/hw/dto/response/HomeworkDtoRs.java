package ru.otus.hw.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HomeworkDtoRs {

    private String id;

    private String text;

    private String studentId;
}
