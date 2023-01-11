package com.demo.escuela.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Calificacion.
 */
@Entity
@Table(name = "calificacion")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Calificacion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "calificacion", nullable = false)
    private Float calificacion;

    @Column(name = "fecha_registro")
    private Instant fechaRegistro;

    @ManyToOne
    @JsonIgnoreProperties(value = { "calificacions" }, allowSetters = true)
    private Materia materia;

    @ManyToOne
    @JsonIgnoreProperties(value = { "calificacions" }, allowSetters = true)
    private Alumno alumno;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Calificacion id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getCalificacion() {
        return this.calificacion;
    }

    public Calificacion calificacion(Float calificacion) {
        this.setCalificacion(calificacion);
        return this;
    }

    public void setCalificacion(Float calificacion) {
        this.calificacion = calificacion;
    }

    public Instant getFechaRegistro() {
        return this.fechaRegistro;
    }

    public Calificacion fechaRegistro(Instant fechaRegistro) {
        this.setFechaRegistro(fechaRegistro);
        return this;
    }

    public void setFechaRegistro(Instant fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public Materia getMateria() {
        return this.materia;
    }

    public void setMateria(Materia materia) {
        this.materia = materia;
    }

    public Calificacion materia(Materia materia) {
        this.setMateria(materia);
        return this;
    }

    public Alumno getAlumno() {
        return this.alumno;
    }

    public void setAlumno(Alumno alumno) {
        this.alumno = alumno;
    }

    public Calificacion alumno(Alumno alumno) {
        this.setAlumno(alumno);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Calificacion)) {
            return false;
        }
        return id != null && id.equals(((Calificacion) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Calificacion{" +
            "id=" + getId() +
            ", calificacion=" + getCalificacion() +
            ", fechaRegistro='" + getFechaRegistro() + "'" +
            "}";
    }
}
