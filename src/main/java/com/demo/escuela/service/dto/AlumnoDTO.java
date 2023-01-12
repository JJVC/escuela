package com.demo.escuela.service.dto;

import com.demo.escuela.domain.Alumno;
import com.demo.escuela.domain.Calificacion;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.HashSet;
import java.util.Set;

public class AlumnoDTO {

    private Long id;
    private String nombre;
    private String apPaterno;
    private String apMaterno;
    private String display;
    private Boolean activo;

    @JsonIgnoreProperties(value = { "alumno" }, allowSetters = true)
    private Set<Calificacion> calificacions = new HashSet<>();

    private Float promedio;

    public AlumnoDTO(Alumno alumno) {
        this.id = alumno.getId();
        this.nombre = alumno.getNombre();
        this.apPaterno = alumno.getApPaterno();
        this.apMaterno = alumno.getApMaterno();
        this.display = alumno.getDisplay();
        this.activo = alumno.getActivo();
        this.calificacions = alumno.getCalificacions();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApPaterno() {
        return apPaterno;
    }

    public void setApPaterno(String apPaterno) {
        this.apPaterno = apPaterno;
    }

    public String getApMaterno() {
        return apMaterno;
    }

    public void setApMaterno(String apMaterno) {
        this.apMaterno = apMaterno;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public Set<Calificacion> getCalificacions() {
        return calificacions;
    }

    public void setCalificacions(Set<Calificacion> calificacions) {
        this.calificacions = calificacions;
    }

    public Float getPromedio() {
        return promedio;
    }

    public void setPromedio(Float promedio) {
        this.promedio = promedio;
    }
}
