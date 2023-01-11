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
 * A Materia.
 */
@Entity
@Table(name = "materia")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Materia implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "activo")
    private Boolean activo;

    @OneToMany(mappedBy = "materia")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "materia", "alumno" }, allowSetters = true)
    private Set<Calificacion> calificacions = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Materia id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public Materia nombre(String nombre) {
        this.setNombre(nombre);
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Boolean getActivo() {
        return this.activo;
    }

    public Materia activo(Boolean activo) {
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
            this.calificacions.forEach(i -> i.setMateria(null));
        }
        if (calificacions != null) {
            calificacions.forEach(i -> i.setMateria(this));
        }
        this.calificacions = calificacions;
    }

    public Materia calificacions(Set<Calificacion> calificacions) {
        this.setCalificacions(calificacions);
        return this;
    }

    public Materia addCalificacion(Calificacion calificacion) {
        this.calificacions.add(calificacion);
        calificacion.setMateria(this);
        return this;
    }

    public Materia removeCalificacion(Calificacion calificacion) {
        this.calificacions.remove(calificacion);
        calificacion.setMateria(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Materia)) {
            return false;
        }
        return id != null && id.equals(((Materia) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Materia{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", activo='" + getActivo() + "'" +
            "}";
    }
}
