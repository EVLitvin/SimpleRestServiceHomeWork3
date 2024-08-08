package ru.evlitvin.rest.api.v1;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.evlitvin.dto.SchoolDTO;
import ru.evlitvin.exception.SchoolNotFoundException;
import ru.evlitvin.service.SchoolService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SchoolControllerTest {

    @InjectMocks
    private SchoolController schoolController;

    @Mock
    private SchoolService schoolService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void givenExistingSchool_whenGetSchoolById_thenReturnSchoolAndHttpStatusOk() throws SchoolNotFoundException {
        SchoolDTO schoolDTO = new SchoolDTO(1L, "School # 1", "School # 1 address");

        when(schoolService.getSchoolById(1L)).thenReturn(schoolDTO);

        ResponseEntity<SchoolDTO> responseEntity = schoolController.getSchoolById(1L);
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(schoolDTO, responseEntity.getBody());
    }

    @Test
    void givenNotExistingSchool_whenGetSchoolById_thenThrowSchoolNotFoundExceptionHttpStatusNotFound() throws SchoolNotFoundException {
        when(schoolService.getSchoolById(1L)).thenThrow(new SchoolNotFoundException("No school found with ID: " + 1));

        ResponseEntity<SchoolDTO> responseEntity = schoolController.getSchoolById(1L);
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    void givenExistingSchools_whenGetAllSchools_thenReturnListOfSchoolsAndHttpStatusOk() throws SchoolNotFoundException {
        List<SchoolDTO> schools = new ArrayList<>();
        schools.add(new SchoolDTO(1L, "School # 1", "School # 1 address"));
        schools.add(new SchoolDTO(2L, "School # 2", "School # 2 address"));

        when(schoolService.getAllSchools()).thenReturn(schools);

        ResponseEntity<List<SchoolDTO>> responseEntity = schoolController.getAllSchools();
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(schools, responseEntity.getBody());
    }

    @Test
    void givenNotExistingSchools_whenGetAllSchools_thenThrowSchoolNotFoundExceptionHttpStatusNotFound() throws SchoolNotFoundException {
        when(schoolService.getAllSchools()).thenThrow(new SchoolNotFoundException("No school found with ID:"  + 1));

        ResponseEntity<List<SchoolDTO>> responseEntity = schoolController.getAllSchools();
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    void givenCorrectSchoolData_whenCreateSchool_thenSaveSchoolAndHttpStatusCreated() throws SchoolNotFoundException {
        SchoolDTO schoolDTO = new SchoolDTO(1L, "School # 1", "School # 1 address");

        doNothing().when(schoolService).createSchool(schoolDTO);

        ResponseEntity<SchoolDTO> responseEntity = schoolController.createSchool(schoolDTO);
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
    }

    @Test
    void givenSchoolFieldsEmpty_whenCreateSchool_thenThrowSchoolNotFoundExceptionAngHttpResponseBadRequest() throws SchoolNotFoundException {
        SchoolDTO schoolDTO = new SchoolDTO(1L, "School # 1", "School # 1 address");

        doThrow(new SchoolNotFoundException("No school found with ID:"  + 1)).when(schoolService).createSchool(schoolDTO);

        ResponseEntity<SchoolDTO> responseEntity = schoolController.createSchool(schoolDTO);
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    void givenExistingSchool_whenUpdateSchool_thenUpdateSchoolAndHttpStatusCreated() throws SchoolNotFoundException {
        SchoolDTO schoolDTO = new SchoolDTO(1L, "School # 1", "School # 1 address");

        doNothing().when(schoolService).updateSchool(1L, schoolDTO);

        ResponseEntity<SchoolDTO> responseEntity = schoolController.updateSchool(1L, schoolDTO);
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
    }

    @Test
    void givenExistingSchool_whenDeleteSchool_thenDeleteSchoolAndHttpStatusNoContent() throws SchoolNotFoundException {
        doNothing().when(schoolService).deleteSchool(1L);

        ResponseEntity<SchoolDTO> responseEntity = schoolController.deleteSchool(1L);
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
    }

    @Test
    void givenSchoolDoesNotExist_whenDeleteSchool_thenThrowSchoolNotFoundExceptionAndHttpStatusNotFound() throws SchoolNotFoundException {
        doThrow(new SchoolNotFoundException("No school found with ID:"  + 1)).when(schoolService).deleteSchool(1L);

        ResponseEntity<SchoolDTO> responseEntity = schoolController.deleteSchool(1L);
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }
}