package com.Valentin.aad.model;

import java.time.LocalDate;

public class Enrollment {

    private Integer studentId;
    private Integer moduleId;
    private LocalDate fecha;

    public Enrollment() { }

    public Enrollment(Integer studentId, Integer moduleId, LocalDate fecha) {
        this.studentId = studentId;
        this.moduleId = moduleId;
        this.fecha = fecha;
    }

    public Integer getStudentId() {
        return studentId;
    }

    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }

    public Integer getModuleId() {
        return moduleId;
    }

    public void setModuleId(Integer moduleId) {
        this.moduleId = moduleId;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }
}