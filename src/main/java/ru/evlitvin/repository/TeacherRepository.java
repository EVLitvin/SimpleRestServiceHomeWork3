package ru.evlitvin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.evlitvin.entity.Teacher;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {
}
