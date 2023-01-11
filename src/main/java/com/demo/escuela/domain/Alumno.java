package com.demo.escuela.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Alumno.
 */
@Entity
@Table(name = "alumno")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Alumno implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "nombre", nullable = false)
    private String nombre;

    @NotNull
    @Column(name = "ap_paterno", nullable = false)
    private String apPaterno;

    @Column(name = "ap_materno")
    private String apMaterno;

    @Column(name = "display")
    private String display;

    @Column(name = "activo")
    private Boolean activo;

    @OneToMany(mappedBy = "alumno")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "materia", "alumno" }, allowSetters = true)
    private Set<Calificacion> calificacions = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Alumno id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public Alumno nombre(String nombre) {
        this.setNombre(nombre);
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApPaterno() {
        return this.apPaterno;
    }

    public Alumno apPaterno(String apPaterno) {
        this.setApPaterno(apPaterno);
        return this;
    }

    public void setApPaterno(String apPaterno) {
        this.apPaterno = apPaterno;
    }

    public String getApMaterno() {
        return this.apMaterno;
    }

    public Alumno apMaterno(String apMaterno) {
        this.setApMaterno(apMaterno);
        return this;
    }

    public void setApMaterno(String apMaterno) {
        this.apMaterno = apMaterno;
    }

    public String getDisplay() {
        return this.display;
    }

    public Alumno display(String display) {
        this.setDisplay(display);
        return this;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public Boolean getActivo() {
        return this.activo;
    }

    public Alumno activo(Boolean activo) {
        this.setActivo(activo);
        return this;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public Set<Calificacion> getCalificacions() {
        return this.calificacions;
    }

    public void setCalificacions(Set<Calificacion> calificacions) {
        if (this.calificacions != null) {
            this.calificacions.forEach(i -> i.setAlumno(null));
        }
        if (calificacions != null) {
            calificacions.forEach(i -> i.setAlumno(this));
        }
        this.calificacions = calificacions;
    }

    public Alumno calificacions(Set<Calificacion> calificacions) {
        this.setCalificacions(calificacions);
        return this;
    }

    public Alumno addCalificacion(Calificacion calificacion) {
        this.calificacions.add(calificacion);
        calificacion.setAlumno(this);
        return this;
    }

    public Alumno removeCalificacion(Calificacion calificacion) {
        this.calificacions.remove(calificacion);
        calificacion.setAlumno(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Alumno)) {
            return false;
        }
        return id != null && id.equals(((Alumno) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Alumno{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", apPaterno='" + getApPaterno() + "'" +
            ", apMaterno='" + getApMaterno() + "'" +
            ", display='" + getDisplay() + "'" +
            ", activo='" + getActivo() + "'" +
            "}";
    }
}
