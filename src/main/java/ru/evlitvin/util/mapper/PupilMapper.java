package ru.evlitvin.util.mapper;

import org.mapstruct.Mapper;
import ru.evlitvin.dto.PupilDTO;
import ru.evlitvin.entity.Pupil;

@Mapper(componentModel = "spring")
public interface PupilMapper {

    PupilDTO toPupilDTO(Pupil pupil);

    Pupil toPupil(PupilDTO pupilDTO);
}
