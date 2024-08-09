package ru.otus.hw.mapper;

import org.mapstruct.Mapper;
import ru.otus.hw.dto.response.SkillDtoRs;
import ru.otus.hw.models.Skill;

@Mapper(componentModel = "spring")
public interface SkillMapper {

    SkillDtoRs entityToDtoRs(Skill skill);
}
