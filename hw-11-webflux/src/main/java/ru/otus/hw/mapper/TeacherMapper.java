package ru.otus.hw.mapper;

import org.mapstruct.Mapper;
import ru.otus.hw.dto.response.TeacherDtoRs;
import ru.otus.hw.models.Teacher;

@Mapper(componentModel = "spring")
public interface TeacherMapper {

    TeacherDtoRs entityToDtoRs(Teacher teacher);
}
