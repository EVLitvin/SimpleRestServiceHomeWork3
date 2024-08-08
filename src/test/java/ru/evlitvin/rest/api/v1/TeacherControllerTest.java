package ru.evlitvin.rest.api.v1;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.evlitvin.dto.TeacherDTO;
import ru.evlitvin.exception.TeacherNotFoundException;
import ru.evlitvin.service.TeacherService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TeacherControllerTest {

    @InjectMocks
    private TeacherController teacherController;

    @Mock
    private TeacherService teacherService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void givenExistingTeacher_whenGetTeacherById_thenReturnTeacherAndHttpStatusOk() throws TeacherNotFoundException {
        TeacherDTO teacherDTO = new TeacherDTO(1L, "Ivan", "Ivanov", 1L);

        when(teacherService.getTeacherById(1L)).thenReturn(teacherDTO);

        ResponseEntity<TeacherDTO> responseEntity = teacherController.getTeacherById(1L);
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(teacherDTO, responseEntity.getBody());
    }

    @Test
    void givenNotExistingTeacher_whenGetTeacherById_thenThrowTeacherNotFoundExceptionHttpStatusNotFound() throws TeacherNotFoundException {
        when(teacherService.getTeacherById(1L)).thenThrow(new TeacherNotFoundException("No teacher found with ID: " + 1));

        ResponseEntity<TeacherDTO> responseEntity = teacherController.getTeacherById(1L);
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    void givenExistingTeachers_whenGetAllTeachers_thenReturnListOfTeachersAndHttpStatusOk() throws TeacherNotFoundException {
        List<TeacherDTO> teachers = new ArrayList<>();
        teachers.add(new TeacherDTO(1L, "Ivan", "Ivanov", 1L));
        teachers.add(new TeacherDTO(2L, "Petr", "Petrov", 1L));

        when(teacherService.getAllTeachers()).thenReturn(teachers);

        ResponseEntity<List<TeacherDTO>> responseEntity = teacherController.getAllTeachers();
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(teachers, responseEntity.getBody());
    }

    @Test
    void givenNotExistingTeachers_whenGetAllTeachers_thenThrowTeacherNotFoundExceptionHttpStatusNotFound() throws TeacherNotFoundException {
        when(teacherService.getAllTeachers()).thenThrow(new TeacherNotFoundException("No teacher found with ID:"  + 1));

        ResponseEntity<List<TeacherDTO>> responseEntity = teacherController.getAllTeachers();
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    void givenCorrectTeacherData_whenCreateTeacher_thenSaveTeacherAndHttpStatusCreated() throws TeacherNotFoundException {
        TeacherDTO teacherDTO = new TeacherDTO(1L, "Ivan", "Ivanov", 1L);

        doNothing().when(teacherService).createTeacher(teacherDTO);

        ResponseEntity<TeacherDTO> responseEntity = teacherController.createTeacher(teacherDTO);
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
    }

    @Test
    void givenTeacherFieldsEmpty_whenCreateTeacher_thenThrowTeacherNotFoundExceptionAngHttpResponseBadRequest() throws TeacherNotFoundException {
        TeacherDTO teacherDTO = new TeacherDTO(1L, "Ivan", "Ivanov", 1L);

        doThrow(new TeacherNotFoundException("No teacher found with ID:"  + 1)).when(teacherService).createTeacher(teacherDTO);

        ResponseEntity<TeacherDTO> responseEntity = teacherController.createTeacher(teacherDTO);
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    void givenExistingTeacher_whenUpdateTeacher_thenUpdateTeacherAndHttpStatusCreated() throws TeacherNotFoundException {
        TeacherDTO teacherDTO = new TeacherDTO(1L, "Ivan", "Ivanov", 1L);

        doNothing().when(teacherService).updateTeacher(1L, teacherDTO);

        ResponseEntity<TeacherDTO> responseEntity = teacherController.updateTeacher(1L, teacherDTO);
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
    }

    @Test
    void givenExistingTeacher_whenDeleteTeacher_thenDeleteTeacherAndHttpStatusNoContent() throws TeacherNotFoundException {
        doNothing().when(teacherService).deleteTeacher(1L);

        ResponseEntity<TeacherDTO> responseEntity = teacherController.deleteTeacher(1L);
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
    }

    @Test
    void givenTeacherDoesNotExist_whenDeleteTeacher_thenThrowTeacherNotFoundExceptionAndHttpStatusNotFound() throws TeacherNotFoundException {
        doThrow(new TeacherNotFoundException("No teacher found with ID:"  + 1)).when(teacherService).deleteTeacher(1L);

        ResponseEntity<TeacherDTO> responseEntity = teacherController.deleteTeacher(1L);
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

}