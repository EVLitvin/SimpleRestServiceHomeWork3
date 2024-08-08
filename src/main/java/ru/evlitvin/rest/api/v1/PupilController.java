package ru.evlitvin.rest.api.v1;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.evlitvin.dto.PupilDTO;
import ru.evlitvin.exception.PupilNotFoundException;
import ru.evlitvin.service.PupilService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/pupil")
public class PupilController {

    private final PupilService pupilService;

    public PupilController(PupilService pupilService) {
        this.pupilService = pupilService;
    }

    @GetMapping(value = "/all", produces = "application/json")
    public ResponseEntity<List<PupilDTO>> getAllPupils() {
        List<PupilDTO> pupils = pupilService.getAllPupils();
        return ResponseEntity.status(HttpStatus.OK).body(pupils);
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<PupilDTO> getPupilById(@PathVariable long id) {
        try {
            pupilService.getPupilById(id);
        } catch (PupilNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(pupilService.getPupilById(id));
    }

    @PostMapping
    public ResponseEntity<?> addPupil(@RequestBody PupilDTO pupilDTO) {
        try {
            pupilService.addPupil(pupilDTO);
        } catch (PupilNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(pupilDTO);
    }

    @PutMapping(value = "/update/{id}", consumes = "application/json")
    public ResponseEntity<PupilDTO> updatePupil(@PathVariable long id, @RequestBody PupilDTO pupilDTO) {
        try {
            pupilService.updatePupil(id, pupilDTO);
        } catch (PupilNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<PupilDTO> deletePupil(@PathVariable long id) {
        try {
            pupilService.deletePupil(id);
        } catch (PupilNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}