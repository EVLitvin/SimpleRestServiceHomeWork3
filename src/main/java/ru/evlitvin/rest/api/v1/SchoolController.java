package ru.evlitvin.rest.api.v1;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.evlitvin.dto.SchoolDTO;
import ru.evlitvin.exception.SchoolNotFoundException;
import ru.evlitvin.service.SchoolService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/school")
public class SchoolController {

    private final SchoolService schoolService;

    public SchoolController(SchoolService schoolService) {
        this.schoolService = schoolService;
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<SchoolDTO> getSchoolById(@PathVariable("id") long id) {
        try {
            SchoolDTO schoolDTO = schoolService.getSchoolById(id);
            return ResponseEntity.ok(schoolDTO);
        } catch (SchoolNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping(value = "/all", produces = "application/json")
    public ResponseEntity<List<SchoolDTO>> getAllSchools() {
        try {
            List<SchoolDTO> schoolDTOList = schoolService.getAllSchools();
            return ResponseEntity.ok(schoolDTOList);
        } catch (SchoolNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping(consumes = "application/json")
    public ResponseEntity<SchoolDTO> createSchool(@RequestBody SchoolDTO schoolDTO) {
        try {
            schoolService.createSchool(schoolDTO);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (SchoolNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping(value = "/update/{id}", consumes = "application/json")
    public ResponseEntity<SchoolDTO> updateSchool(@PathVariable("id") long id, @RequestBody SchoolDTO schoolDTO) {
        try {
            schoolService.updateSchool(id, schoolDTO);
        } catch (SchoolNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<SchoolDTO> deleteSchool(@PathVariable("id") long id) {
        try {
            schoolService.deleteSchool(id);
        } catch (SchoolNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.noContent().build();
    }

}
