package ru.evlitvin.util.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.evlitvin.dto.TeacherDTO;
import ru.evlitvin.entity.Teacher;

@Mapper(componentModel = "spring")
public interface TeacherMapper {

    @Mapping(source = "school.id", target = "schoolId")
    TeacherDTO toTeacherDTO(Teacher teacher);

    @Mapping(source = "schoolId", target = "school.id")
    Teacher toTeacher(TeacherDTO teacherDTO);
}
