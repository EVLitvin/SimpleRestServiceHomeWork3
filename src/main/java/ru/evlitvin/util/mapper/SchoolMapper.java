package ru.evlitvin.util.mapper;

import org.mapstruct.Mapper;
import ru.evlitvin.dto.SchoolDTO;
import ru.evlitvin.entity.School;
import ru.evlitvin.service.SchoolService;

@Mapper(componentModel = "spring", uses = SchoolService.class)
public interface SchoolMapper {

    SchoolDTO toSchoolDTO(School school);

    School toSchool(SchoolDTO schoolDTO);
}
