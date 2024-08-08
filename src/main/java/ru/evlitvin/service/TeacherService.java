package ru.evlitvin.service;

import org.springframework.stereotype.Service;
import ru.evlitvin.dto.TeacherDTO;
import ru.evlitvin.entity.Teacher;
import ru.evlitvin.exception.TeacherNotFoundException;
import ru.evlitvin.repository.TeacherRepository;
import ru.evlitvin.util.mapper.TeacherMapper;

import java.util.List;

@Service
public class TeacherService {

    private final TeacherRepository teacherRepository;

    private final TeacherMapper teacherMapper;

    public TeacherService(TeacherRepository teacherRepository, TeacherMapper teacherMapper) {
        this.teacherRepository = teacherRepository;
        this.teacherMapper = teacherMapper;
    }

    public void createTeacher(TeacherDTO teacherDTO) {
        Teacher teacher = teacherMapper.toTeacher(teacherDTO);
        if (teacher.getFirstName() == null || teacher.getLastName() == null) {
            throw new IllegalArgumentException("Teacher firstname and lastname are required");
        }
        teacherRepository.save(teacher);
    }

    public TeacherDTO getTeacherById(Long id) {
        Teacher teacher = teacherRepository.findById(id).orElseThrow(() ->
                new TeacherNotFoundException("No teacher found with ID: " + id));
        return teacherMapper.toTeacherDTO(teacher);
    }

    public List<TeacherDTO> getAllTeachers() {
        List<TeacherDTO> teachers = teacherRepository.findAll().stream()
                .map(teacherMapper::toTeacherDTO)
                .toList();
        if (teachers.isEmpty()) {
            throw new TeacherNotFoundException("No teachers in database");
        }
        return teachers;
    }

    public void updateTeacher(long id, TeacherDTO teacherDTO) {
        Teacher teacher = teacherRepository.findById(id).orElseThrow(() -> new TeacherNotFoundException(
                "No teacher found with ID: " + id)
        );
        if (teacherDTO.getFirstName() != null) {
            teacher.setFirstName(teacherDTO.getFirstName());
        }
        if (teacherDTO.getLastName() != null) {
            teacher.setLastName(teacherDTO.getLastName());
        }
        teacherRepository.save(teacher);
    }

    public void deleteTeacher(Long id) {
        if (teacherRepository.findById(id).isPresent()) {
            teacherRepository.deleteById(id);
        } else {
            throw new TeacherNotFoundException("No teacher found with ID: " + id);
        }
    }
}
