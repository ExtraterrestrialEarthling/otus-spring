package ru.otus.hw.mapper;

import org.mapstruct.Mapper;
import ru.otus.hw.dto.response.TeacherDtoRs;
import ru.otus.hw.dto.response.StudentDtoRs;
import ru.otus.hw.dto.response.SkillDtoRs;
import ru.otus.hw.models.Teacher;
import ru.otus.hw.models.Student;
import ru.otus.hw.models.Skill;

@Mapper(componentModel = "spring")
public interface StudentMapper {

    StudentDtoRs entityToDtoRs(Student student);

    TeacherDtoRs entityToDtoRs(Teacher teacher);

    SkillDtoRs entityToDtoRs(Skill skill);
}