package ru.evlitvin.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.evlitvin.dto.TeacherDTO;
import ru.evlitvin.entity.Teacher;
import ru.evlitvin.exception.TeacherNotFoundException;
import ru.evlitvin.repository.TeacherRepository;
import ru.evlitvin.util.mapper.TeacherMapper;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TeacherServiceTest {

    @Mock
    private TeacherRepository teacherRepository;

    @Mock
    private TeacherMapper teacherMapper;

    @InjectMocks
    private TeacherService teacherService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void givenExistingTeacher_whenGetTeacherById_thenReturnTeacher() {
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        teacher.setFirstName("Ivan");
        teacher.setLastName("Ivanov");

        TeacherDTO teacherDTO = new TeacherDTO();
        teacherDTO.setId(1L);
        teacherDTO.setFirstName("Ivan");
        teacherDTO.setLastName("Ivanov");

        when(teacherRepository.findById(1L)).thenReturn(Optional.of(teacher));
        when(teacherMapper.toTeacherDTO(teacher)).thenReturn(teacherDTO);

        TeacherDTO returnedTeacher = teacherService.getTeacherById(1L);

        assertNotNull(returnedTeacher);
        assertEquals(1L, returnedTeacher.getId());
        assertEquals("Ivan", returnedTeacher.getFirstName());
        assertEquals("Ivanov", returnedTeacher.getLastName());
    }

    @Test
    void givenNotExistingTeacher_whenGetTeacherById_thenThrowTeacherNotFoundException() {
        when(teacherRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(TeacherNotFoundException.class, () -> teacherService.getTeacherById(1L));
    }

    @Test
    void givenExistingTeachers_whenGetAllTeachers_thenReturnListOfTeachers() {
        Teacher teacherOne = new Teacher();
        teacherOne.setId(1L);
        teacherOne.setFirstName("Ivan");
        teacherOne.setLastName("Ivanov");

        Teacher teacherTwo = new Teacher();
        teacherTwo.setId(2L);
        teacherTwo.setFirstName("Petr");
        teacherTwo.setLastName("Petrov");

        TeacherDTO teacherDTOOne = new TeacherDTO();
        teacherDTOOne.setId(1L);
        teacherDTOOne.setFirstName("Ivan");
        teacherDTOOne.setLastName("Ivanov");

        TeacherDTO teacherDTOTwo = new TeacherDTO();
        teacherDTOTwo.setId(2L);
        teacherDTOTwo.setFirstName("Petr");
        teacherDTOTwo.setLastName("Petrov");

        when(teacherRepository.findAll()).thenReturn(Arrays.asList(teacherOne, teacherTwo));
        when(teacherMapper.toTeacherDTO(teacherOne)).thenReturn(teacherDTOOne);
        when(teacherMapper.toTeacherDTO(teacherTwo)).thenReturn(teacherDTOTwo);

        List<TeacherDTO> teacherDTOList = teacherService.getAllTeachers();

        assertNotNull(teacherDTOList);
        assertEquals(2, teacherDTOList.size());
    }

    @Test
    void givenNotExistingTeachers_whenGetAllTeachers_thenThrowTeacherNotFoundException() {
        when(teacherRepository.findAll()).thenReturn(List.of());

        assertThrows(TeacherNotFoundException.class, () -> teacherService.getAllTeachers());
    }

    @Test
    void givenCorrectTeacherData_whenCreateTeacher_thenSaveTeacher() {
        TeacherDTO teacherDTO = new TeacherDTO();
        teacherDTO.setFirstName("Ivan");
        teacherDTO.setLastName("Ivanov");

        Teacher teacher = new Teacher();
        teacher.setFirstName("Ivan");
        teacher.setLastName("Ivanov");

        when(teacherMapper.toTeacher(teacherDTO)).thenReturn(teacher);

        teacherService.createTeacher(teacherDTO);

        verify(teacherRepository, times(1)).save(teacher);
    }

    @Test
    void givenTeacherFieldsEmpty_whenCreateTeacher_thenThrowIllegalArgumentException() {
        TeacherDTO teacherDTO = new TeacherDTO();
        Teacher teacher = new Teacher();

        when(teacherMapper.toTeacher(teacherDTO)).thenReturn(teacher);
        assertThrows(IllegalArgumentException.class, () -> teacherService.createTeacher(teacherDTO));
    }

    @Test
    void givenTeacherFirstNameEmpty_whenCreateTeacher_thenThrowIllegalArgumentException() {
        TeacherDTO teacherDTO = new TeacherDTO();
        Teacher teacher = new Teacher();
        teacher.setLastName("Ivanov");

        when(teacherMapper.toTeacher(teacherDTO)).thenReturn(teacher);
        assertThrows(IllegalArgumentException.class, () -> teacherService.createTeacher(teacherDTO));
    }

    @Test
    void givenTeacherLastNameEmpty_whenCreateTeacher_thenThrowIllegalArgumentException() {
        TeacherDTO teacherDTO = new TeacherDTO();
        Teacher teacher = new Teacher();
        teacher.setFirstName("Ivan");

        when(teacherMapper.toTeacher(teacherDTO)).thenReturn(teacher);
        assertThrows(IllegalArgumentException.class, () -> teacherService.createTeacher(teacherDTO));
    }

    @Test
    void givenExistingTeacher_whenUpdateTeacher_thenUpdateTeacher() {
        TeacherDTO teacherDTO = new TeacherDTO();
        teacherDTO.setFirstName("Petr");
        teacherDTO.setLastName("Petrov");

        Teacher teacher = new Teacher();
        teacher.setId(1L);
        teacher.setFirstName("Ivan");
        teacher.setLastName("Ivanov");

        when(teacherRepository.findById(1L)).thenReturn(Optional.of(teacher));

        teacherService.updateTeacher(1L, teacherDTO);

        assertEquals("Petr", teacher.getFirstName());
        assertEquals("Petrov", teacher.getLastName());
        verify(teacherRepository, times(1)).save(teacher);
    }

    @Test
    void givenNotExistingTeacher_thenUpdateTeacher_thenThrowTeacherNotFoundException() {
        TeacherDTO teacherDTO = new TeacherDTO();

        when(teacherRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(TeacherNotFoundException.class, () -> teacherService.updateTeacher(1L, teacherDTO));
    }

    @Test
    void givenExistingTeacher_whenDeleteTeacher_thenDeleteTeacher() {
        Teacher teacher = new Teacher();
        teacher.setId(1L);

        when(teacherRepository.findById(1L)).thenReturn(Optional.of(teacher));

        teacherService.deleteTeacher(1L);

        verify(teacherRepository, times(1)).deleteById(1L);
    }

    @Test
    void givenNotExistingTeacher_whenDeleteTeacher_ThenThrowTeacherNotFoundException() {
        when(teacherRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(TeacherNotFoundException.class, () -> teacherService.deleteTeacher(1L));
    }
}