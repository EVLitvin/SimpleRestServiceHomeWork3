package ru.evlitvin.rest.api.v1;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.evlitvin.dto.TeacherDTO;
import ru.evlitvin.exception.TeacherNotFoundException;
import ru.evlitvin.service.TeacherService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/teacher")
public class TeacherController {

    private final TeacherService teacherService;

    public TeacherController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<TeacherDTO> getTeacherById(@PathVariable("id") long id) {
        try {
            TeacherDTO teacherDTO = teacherService.getTeacherById(id);
            return ResponseEntity.ok(teacherDTO);
        } catch (TeacherNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping(value = "/all", produces = "application/json")
    public ResponseEntity<List<TeacherDTO>> getAllTeachers() {
        try {
            List<TeacherDTO> teacherDTOList = teacherService.getAllTeachers();
            return ResponseEntity.ok(teacherDTOList);
        } catch (TeacherNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping(consumes = "application/json")
    public ResponseEntity<TeacherDTO> createTeacher(@RequestBody TeacherDTO teacherDTO) {
        try {
            teacherService.createTeacher(teacherDTO);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (TeacherNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping(value = "/update/{id}", consumes = "application/json")
    public ResponseEntity<TeacherDTO> updateTeacher(@PathVariable("id") long id, @RequestBody TeacherDTO teacherDTO) {
        try {
            teacherService.updateTeacher(id, teacherDTO);
        } catch (TeacherNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<TeacherDTO> deleteTeacher(@PathVariable("id") long id) {
        try {
            teacherService.deleteTeacher(id);
        } catch (TeacherNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.noContent().build();
    }

}
