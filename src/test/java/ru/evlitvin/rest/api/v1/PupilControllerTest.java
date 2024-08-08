package ru.evlitvin.rest.api.v1;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.evlitvin.dto.PupilDTO;
import ru.evlitvin.exception.PupilNotFoundException;
import ru.evlitvin.service.PupilService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PupilControllerTest {

    @InjectMocks
    private PupilController pupilController;

    @Mock
    private PupilService pupilService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void givenExistingPupils_whenGetAllPupils_thenReturnListOfPupilsAndHttpStatusOk() {
        List<PupilDTO> pupils = new ArrayList<>();
        pupils.add(new PupilDTO(1L, "Ivan", "Ivanov"));
        pupils.add(new PupilDTO(2L, "Petr", "Petrov"));

        when(pupilService.getAllPupils()).thenReturn(pupils);

        ResponseEntity<List<PupilDTO>> responseEntity = pupilController.getAllPupils();
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(pupils, responseEntity.getBody());
    }

    @Test
    void givenExistingPupil_whenGetPupilById_thenReturnPupilAndHttpStatusOk() throws PupilNotFoundException {
        PupilDTO pupilDTO = new PupilDTO(1L, "Ivan", "Ivanov");

        ResponseEntity<PupilDTO> responseEntity = pupilController.getPupilById(pupilDTO.getId());
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void givenNotExistingPupil_whenGetPupilById_thenThrowPupilNotFoundExceptionAndHttpStatusNotFound() throws PupilNotFoundException {
        when(pupilService.getPupilById(1L)).thenThrow(new PupilNotFoundException("No pupil found with ID: " + 1L));

        ResponseEntity<PupilDTO> responseEntity = pupilController.getPupilById(1L);
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    void givenCorrectPupilData_whenAddPupil_thenSavePupilAndReturnHttpStatusCreated() throws PupilNotFoundException {
        PupilDTO pupilDTO = new PupilDTO(1L, "Ivan", "Ivanov");

        doNothing().when(pupilService).addPupil(pupilDTO);

        ResponseEntity<?> responseEntity = pupilController.addPupil(pupilDTO);
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(pupilDTO, responseEntity.getBody());
    }

    @Test
    void givenPupil_whenAddPupil_thenThrowPupilNotFoundExceptionAndHttpStatusBadRequest() throws PupilNotFoundException {
        PupilDTO pupilDTO = new PupilDTO(1L, "Ivan", "Ivanov");

        doThrow(new PupilNotFoundException("No pupil found with ID: " + 1L)).when(pupilService).addPupil(pupilDTO);

        ResponseEntity<?> responseEntity = pupilController.addPupil(pupilDTO);
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    void givenExistingPupil_whenUpdatePupil_thenUpdatePupilAndHttpStatusCreated() throws PupilNotFoundException {
        PupilDTO pupilDTO = new PupilDTO(1L, "Ivan", "Ivanov");

        doNothing().when(pupilService).updatePupil(1L, pupilDTO);

        ResponseEntity<PupilDTO> responseEntity = pupilController.updatePupil(1L, pupilDTO);
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
    }

    @Test
    void givenExistingPupil_whenUpdatePupil_thenThrowPupilNotFoundExceptionAndHttpStatusNotFound() throws PupilNotFoundException {
        PupilDTO pupilDTO = new PupilDTO(1L, "Ivan", "Ivanov");

        doThrow(new PupilNotFoundException("No pupil found with ID: " + 1L)).when(pupilService).updatePupil(1L, pupilDTO);

        ResponseEntity<PupilDTO> responseEntity = pupilController.updatePupil(1L, pupilDTO);
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    void givenExistingPupil_whenDeletePupilById_thenDeletePupilAndHttpStatusNoContent() throws PupilNotFoundException {
        doNothing().when(pupilService).deletePupil(1L);

        ResponseEntity<PupilDTO> responseEntity = pupilController.deletePupil(1L);
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
    }

    @Test
    void givenExistingPupil_whenDeletePupilById_thenThrowPupilNotFoundExceptionAndHttpStatusNotFound() throws PupilNotFoundException {
        doThrow(new PupilNotFoundException("No pupil found with ID: " + 1L)).when(pupilService).deletePupil(1L);

        ResponseEntity<PupilDTO> responseEntity = pupilController.deletePupil(1L);
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }
}