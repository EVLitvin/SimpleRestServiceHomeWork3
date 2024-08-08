package ru.evlitvin.service;

import org.springframework.stereotype.Service;
import ru.evlitvin.dto.SchoolDTO;
import ru.evlitvin.entity.School;
import ru.evlitvin.exception.SchoolNotFoundException;
import ru.evlitvin.repository.SchoolRepository;
import ru.evlitvin.util.mapper.SchoolMapper;

import java.util.List;

@Service
public class SchoolService {

    private final SchoolRepository schoolRepository;

    private final SchoolMapper schoolMapper;

    public SchoolService(SchoolRepository schoolRepository, SchoolMapper schoolMapper) {
        this.schoolRepository = schoolRepository;
        this.schoolMapper = schoolMapper;
    }

    public void createSchool(SchoolDTO schoolDTO) {
        School school = schoolMapper.toSchool(schoolDTO);
        if (school.getSchoolName() == null || school.getAddress() == null) {
            throw new IllegalArgumentException("School name and address are required");
        }
        schoolRepository.save(school);
    }

    public SchoolDTO getSchoolById(Long id) {
        School school = schoolRepository.findById(id).orElseThrow(() ->
                new SchoolNotFoundException("No school found with ID: " + id));
        return schoolMapper.toSchoolDTO(school);
    }

    public List<SchoolDTO> getAllSchools() {
        List<SchoolDTO> schools = schoolRepository.findAll().stream()
                .map(schoolMapper::toSchoolDTO)
                .toList();
        if (schools.isEmpty()) {
            throw new SchoolNotFoundException("No schools in database");
        }
        return schools;
    }

    public void updateSchool(long id, SchoolDTO schoolDTO) {
        School school = schoolRepository.findById(id).orElseThrow(() -> new SchoolNotFoundException(
                "No school found with ID: " + id)
        );
        if (schoolDTO.getSchoolName() != null) {
            school.setSchoolName(schoolDTO.getSchoolName());
        }
        if (schoolDTO.getAddress() != null) {
            school.setAddress(schoolDTO.getAddress());
        }
        schoolRepository.save(school);
    }

    public void deleteSchool(Long id) {
        if (schoolRepository.findById(id).isPresent()) {
            schoolRepository.deleteById(id);
        } else {
            throw new SchoolNotFoundException("No school found with ID: " + id);
        }
    }
}
