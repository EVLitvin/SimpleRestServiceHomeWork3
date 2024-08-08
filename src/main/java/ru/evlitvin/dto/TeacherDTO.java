package ru.evlitvin.dto;

import ru.evlitvin.entity.Pupil;

import java.util.List;

public class TeacherDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private Long schoolId;
    private List<Pupil> pupils;

    public TeacherDTO() {}

    public TeacherDTO(Long id, String firstName, String lastName, Long schoolId) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.schoolId = schoolId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Long getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(Long schoolId) {
        this.schoolId = schoolId;
    }

}
