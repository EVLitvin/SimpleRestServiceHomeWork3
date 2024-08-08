package ru.evlitvin.dto;

import java.util.List;

public class SchoolDTO {

    private Long id;
    private String schoolName;
    private String address;
    private List<TeacherDTO> teachers;

    public SchoolDTO() {}

    public SchoolDTO(Long id, String schoolName, String address) {
        this.id = id;
        this.schoolName = schoolName;
        this.address = address;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<TeacherDTO> getTeachers() {
        return teachers;
    }

    public void setTeachers(List<TeacherDTO> teachers) {
        this.teachers = teachers;
    }

}
