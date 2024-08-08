package ru.evlitvin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.evlitvin.entity.Pupil;

public interface PupilRepository extends JpaRepository<Pupil, Long> {
}
