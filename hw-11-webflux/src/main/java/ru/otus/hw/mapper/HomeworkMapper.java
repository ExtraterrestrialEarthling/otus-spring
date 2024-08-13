package ru.otus.hw.mapper;

import org.mapstruct.Mapper;
import ru.otus.hw.dto.response.HomeworkDtoRs;
import ru.otus.hw.models.Homework;

@Mapper(componentModel = "spring")
public interface HomeworkMapper {

    default HomeworkDtoRs entityToDtoRs(Homework homework) {
        return new HomeworkDtoRs(homework.getId(), homework.getText(), homework.getStudent().getId());
    }
}
