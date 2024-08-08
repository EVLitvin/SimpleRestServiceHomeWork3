package ru.evlitvin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.evlitvin.entity.School;

public interface SchoolRepository extends JpaRepository<School, Long> {
}
