package com.demo.escuela.service;

import com.demo.escuela.domain.Alumno;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link Alumno}.
 */
public interface AlumnoService {
    /**
     * Save a alumno.
     *
     * @param alumno the entity to save.
     * @return the persisted entity.
     */
    Alumno save(Alumno alumno);

    /**
     * Updates a alumno.
     *
     * @param alumno the entity to update.
     * @return the persisted entity.
     */
    Alumno update(Alumno alumno);

    /**
     * Partially updates a alumno.
     *
     * @param alumno the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Alumno> partialUpdate(Alumno alumno);

    /**
     * Get all the alumnos.
     *
     * @return the list of entities.
     */
    List<Alumno> findAll();

    /**
     * Get the "id" alumno.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Alumno> findOne(Long id);

    /**
     * Delete the "id" alumno.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    Optional<Alumno> findOneWithEagerRelationships(Long id);
}
