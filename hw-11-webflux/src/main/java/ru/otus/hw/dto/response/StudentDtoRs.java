package ru.otus.hw.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentDtoRs {

    private String id;

    private String name;

    private TeacherDtoRs teacher;

    private List<SkillDtoRs> skills;
}
