package ru.evlitvin.service;

import org.springframework.stereotype.Service;
import ru.evlitvin.dto.PupilDTO;
import ru.evlitvin.entity.Pupil;
import ru.evlitvin.exception.PupilNotFoundException;
import ru.evlitvin.repository.PupilRepository;
import ru.evlitvin.util.mapper.PupilMapper;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PupilService {

    private final PupilMapper pupilMapper;

    private final PupilRepository pupilRepository;

    public PupilService(PupilMapper pupilMapper, PupilRepository pupilRepository) {
        this.pupilMapper = pupilMapper;
        this.pupilRepository = pupilRepository;
    }

    public PupilDTO getPupilById(long id) {
        return pupilMapper.toPupilDTO(pupilRepository.findById(id).orElseThrow(() -> new PupilNotFoundException("No pupil found with ID: " + id)));
    }

    public List<PupilDTO> getAllPupils() {
        return pupilRepository.findAll().stream()
                .map(pupilMapper::toPupilDTO)
                .collect(Collectors.toList());
    }

    public void addPupil(PupilDTO pupilDTO) {
        Pupil pupil = pupilMapper.toPupil(pupilDTO);
        pupilRepository.save(pupil);
    }

    public void updatePupil(long id, PupilDTO pupilDTO) {
        Pupil pupil = pupilRepository.findById(id).orElseThrow(
                () -> new PupilNotFoundException("No pupil found with ID: " + id)
        );
        if (pupilDTO.getFirstName() != null) {
            pupil.setFirstName(pupilDTO.getFirstName());
        }
        if (pupilDTO.getLastName() != null) {
            pupil.setLastName(pupilDTO.getLastName());
        }
        pupilRepository.save(pupil);
    }

    public void deletePupil(long id) {
        pupilRepository.deleteById(id);
    }
}