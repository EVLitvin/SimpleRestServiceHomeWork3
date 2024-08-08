package ru.evlitvin.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.evlitvin.dto.PupilDTO;
import ru.evlitvin.entity.Pupil;
import ru.evlitvin.exception.PupilNotFoundException;
import ru.evlitvin.repository.PupilRepository;
import ru.evlitvin.util.mapper.PupilMapper;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PupilServiceTest {

    @Mock
    private PupilRepository pupilRepository;

    @Mock
    private PupilMapper pupilMapper;

    @InjectMocks
    private PupilService pupilService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void givenExistingPupil_whenGetPupilById_thenReturnPupil() {
        PupilDTO pupilDTO = new PupilDTO();
        Pupil pupil = new Pupil();

        when(pupilMapper.toPupilDTO(pupil)).thenReturn(pupilDTO);
        when(pupilRepository.findById(1L)).thenReturn(Optional.of(pupil));

        PupilDTO returnedPupil = pupilService.getPupilById(1L);

        assertNotNull(returnedPupil);
        verify(pupilRepository).findById(1L);
    }

    @Test
    void givenNotExistingPupil_whenGetPupilById_thenThrowPupilNotFoundException() {
        when(pupilRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(PupilNotFoundException.class, () -> pupilService.getPupilById(1L));
    }

    @Test
     void givenExistingPupils_whenGetAllPupils_thenReturnListOfPupils() {
        Pupil pupilOne = new Pupil();
        pupilOne.setFirstName("Ivan");
        pupilOne.setLastName("Ivanov");

        Pupil pupilTwo = new Pupil();
        pupilTwo.setFirstName("Petr");
        pupilTwo.setLastName("Petrov");

        PupilDTO pupilDTOOne = new PupilDTO();
        pupilDTOOne.setFirstName("Ivan");
        pupilDTOOne.setLastName("Ivanov");

        PupilDTO pupilDTOTwo = new PupilDTO();
        pupilDTOTwo.setFirstName("Petr");
        pupilDTOTwo.setLastName("Petrov");

        when(pupilRepository.findAll()).thenReturn(List.of(pupilOne, pupilTwo));
        when(pupilMapper.toPupilDTO(pupilOne)).thenReturn(pupilDTOOne);
        when(pupilMapper.toPupilDTO(pupilTwo)).thenReturn(pupilDTOTwo);

        List<PupilDTO> pupilDTOList = pupilService.getAllPupils();

        assertNotNull(pupilDTOList);
        assertFalse(pupilDTOList.isEmpty());
        assertEquals(2, pupilDTOList.size());
        verify(pupilRepository).findAll();
        verify(pupilMapper).toPupilDTO(pupilOne);
    }

    @Test
    void givenCorrectPupilData_whenAddPupil_thenSavePupil() {
        PupilDTO pupilDTO = new PupilDTO();
        Pupil pupil = new Pupil();

        when(pupilMapper.toPupil(pupilDTO)).thenReturn(pupil);

        pupilService.addPupil(pupilDTO);

        verify(pupilMapper).toPupil(pupilDTO);
        verify(pupilRepository, times(1)).save(pupil);
    }

    @Test
    void givenExistingPupil_whenUpdatePupil_thenUpdatePupil() {
        PupilDTO pupilDTO = new PupilDTO();
        pupilDTO.setFirstName("Ivan");
        pupilDTO.setLastName("Ivanov");

        Pupil pupil = new Pupil();
        pupil.setFirstName("Petr");
        pupil.setLastName("Petrov");

        when(pupilRepository.findById(1L)).thenReturn(Optional.of(pupil));

        pupilService.updatePupil(1L, pupilDTO);

        assertEquals("Ivan", pupil.getFirstName());
        assertEquals("Ivanov", pupil.getLastName());
    }

    @Test
    void givenExistingPupil_whenUpdatePupilWithoutFirstName_thenUpdatePupil() {
        PupilDTO pupilDTO = new PupilDTO();
        pupilDTO.setLastName("Ivanov");

        Pupil pupil = new Pupil();
        pupil.setFirstName("Petr");
        pupil.setLastName("Petrov");

        when(pupilRepository.findById(1L)).thenReturn(Optional.of(pupil));

        pupilService.updatePupil(1L, pupilDTO);

        assertEquals("Petr", pupil.getFirstName());
        assertEquals("Ivanov", pupil.getLastName());
    }

    @Test
    void givenExistingPupil_whenUpdatePupilWithoutLastName_thenUpdatePupil() {
        PupilDTO pupilDTO = new PupilDTO();
        pupilDTO.setFirstName("Ivan");

        Pupil pupil = new Pupil();
        pupil.setFirstName("Petr");
        pupil.setLastName("Petrov");

        when(pupilRepository.findById(1L)).thenReturn(Optional.of(pupil));

        pupilService.updatePupil(1L, pupilDTO);

        assertEquals("Ivan", pupil.getFirstName());
        assertEquals("Petrov", pupil.getLastName());
    }

    @Test
    void givenNotExistingPupil_thenUpdatePupil_thenThrowPupilNotFoundException() {
        PupilDTO pupilDTO = new PupilDTO();

        when(pupilRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(PupilNotFoundException.class, () -> pupilService.updatePupil(1L, pupilDTO));
    }

    @Test
    void givenExistingPupil_whenDeletePupilById_thenDeletePupil() {
        pupilService.deletePupil(1L);

        verify(pupilRepository, times(1)).deleteById(1L);
    }

}