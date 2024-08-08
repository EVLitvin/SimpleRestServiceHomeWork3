package ru.evlitvin.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.evlitvin.dto.SchoolDTO;
import ru.evlitvin.entity.School;
import ru.evlitvin.exception.SchoolNotFoundException;
import ru.evlitvin.repository.SchoolRepository;
import ru.evlitvin.util.mapper.SchoolMapper;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SchoolServiceTest {

    @Mock
    private SchoolRepository schoolRepository;

    @Mock
    private SchoolMapper schoolMapper;

    @InjectMocks
    private SchoolService schoolService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void givenExistingSchool_whenGetSchoolById_thenReturnSchool() {
        School school = new School();
        school.setId(1L);
        school.setSchoolName("School # 1");
        school.setAddress("School # 1 address");

        SchoolDTO schoolDTO = new SchoolDTO();
        schoolDTO.setId(1L);
        schoolDTO.setSchoolName("School # 1");
        schoolDTO.setAddress("School # 1 address");

        when(schoolRepository.findById(1L)).thenReturn(Optional.of(school));
        when(schoolMapper.toSchoolDTO(school)).thenReturn(schoolDTO);

        SchoolDTO returnedSchool = schoolService.getSchoolById(1L);

        assertNotNull(returnedSchool);
        assertEquals(1L, returnedSchool.getId());
        assertEquals("School # 1", returnedSchool.getSchoolName());
        assertEquals("School # 1 address", returnedSchool.getAddress());
    }

    @Test
    void givenNotExistingSchool_whenGetSchoolById_thenThrowSchoolNotFoundException() {
        when(schoolRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(SchoolNotFoundException.class, () -> schoolService.getSchoolById(1L));
    }

    @Test
    void givenExistingSchools_whenGetAllSchools_thenReturnListOfSchools() {
        School schoolOne = new School();
        schoolOne.setId(1L);
        schoolOne.setSchoolName("School # 1");
        schoolOne.setAddress("School # 1 address");

        School schoolTwo = new School();
        schoolTwo.setId(2L);
        schoolTwo.setSchoolName("School # 2");
        schoolTwo.setAddress("School # 2 address");

        SchoolDTO schoolDTOOne = new SchoolDTO();
        schoolDTOOne.setId(1L);
        schoolDTOOne.setSchoolName("School # 1");
        schoolDTOOne.setAddress("School # 1 address");

        SchoolDTO schoolDTOTwo = new SchoolDTO();
        schoolDTOTwo.setId(2L);
        schoolDTOTwo.setSchoolName("School # 2");
        schoolDTOTwo.setAddress("School # 2 address");

        when(schoolRepository.findAll()).thenReturn(Arrays.asList(schoolOne, schoolTwo));
        when(schoolMapper.toSchoolDTO(schoolOne)).thenReturn(schoolDTOOne);
        when(schoolMapper.toSchoolDTO(schoolTwo)).thenReturn(schoolDTOTwo);

        List<SchoolDTO> schoolDTOList = schoolService.getAllSchools();

        assertNotNull(schoolDTOList);
        assertEquals(2, schoolDTOList.size());
    }

    @Test
    void givenNotExistingSchools_whenGetAllSchools_thenThrowSchoolNotFoundException() {
        when(schoolRepository.findAll()).thenReturn(List.of());

        assertThrows(SchoolNotFoundException.class, () -> schoolService.getAllSchools());
    }

    @Test
    void givenCorrectSchoolData_whenCreateSchool_thenSaveSchool() {
        SchoolDTO schoolDTO = new SchoolDTO();
        schoolDTO.setSchoolName("School # 1");
        schoolDTO.setAddress("School # 1 address");

        School school = new School();
        school.setSchoolName("School # 1");
        school.setAddress("School # 1 address");

        when(schoolMapper.toSchool(schoolDTO)).thenReturn(school);

        schoolService.createSchool(schoolDTO);

        verify(schoolRepository, times(1)).save(school);
    }

    @Test
    void givenSchoolFieldsEmpty_whenCreateSchool_thenThrowIllegalArgumentException() {
        SchoolDTO schoolDTO = new SchoolDTO();
        School school = new School();

        when(schoolMapper.toSchool(schoolDTO)).thenReturn(school);
        assertThrows(IllegalArgumentException.class, () -> schoolService.createSchool(schoolDTO));
    }

    @Test
    void givenSchoolNameEmpty_whenCreateSchool_thenThrowIllegalArgumentException() {
        SchoolDTO schoolDTO = new SchoolDTO();
        School school = new School();
        school.setAddress("School # 1 address");

        when(schoolMapper.toSchool(schoolDTO)).thenReturn(school);
        assertThrows(IllegalArgumentException.class, () -> schoolService.createSchool(schoolDTO));
    }

    @Test
    void givenSchoolAddressEmpty_whenCreateSchool_thenThrowIllegalArgumentException() {
        SchoolDTO schoolDTO = new SchoolDTO();
        School school = new School();
        school.setSchoolName("School # 1");

        when(schoolMapper.toSchool(schoolDTO)).thenReturn(school);
        assertThrows(IllegalArgumentException.class, () -> schoolService.createSchool(schoolDTO));
    }

    @Test
    void givenExistingSchool_whenUpdateSchool_thenUpdateSchool() {
        SchoolDTO schoolDTO = new SchoolDTO();
        schoolDTO.setSchoolName("School # 2");
        schoolDTO.setAddress("School # 2 address");

        School school = new School();
        school.setId(1L);
        school.setSchoolName("School # 1");
        school.setAddress("School # 1 address");

        when(schoolRepository.findById(1L)).thenReturn(Optional.of(school));

        schoolService.updateSchool(1L, schoolDTO);

        assertEquals("School # 2", school.getSchoolName());
        assertEquals("School # 2 address", school.getAddress());
        verify(schoolRepository, times(1)).save(school);
    }

    @Test
    void givenNotExistingSchool_thenUpdateSchool_thenThrowSchoolNotFoundException() {
        SchoolDTO schoolDTO = new SchoolDTO();

        when(schoolRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(SchoolNotFoundException.class, () -> schoolService.updateSchool(1L, schoolDTO));
    }

    @Test
    void givenExistingSchool_whenDeleteSchool_thenDeleteSchool() {
        School school = new School();
        school.setId(1L);

        when(schoolRepository.findById(1L)).thenReturn(Optional.of(school));

        schoolService.deleteSchool(1L);

        verify(schoolRepository, times(1)).deleteById(1L);
    }

    @Test
    void givenSchoolDoesNotExist_whenDeleteSchool_thenThrowSchoolNotFoundException() {
        when(schoolRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(SchoolNotFoundException.class, () -> schoolService.deleteSchool(1L));
    }
}