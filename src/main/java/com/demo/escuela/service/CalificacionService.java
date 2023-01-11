package com.demo.escuela.service;

import com.demo.escuela.domain.Calificacion;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Calificacion}.
 */
public interface CalificacionService {
    /**
     * Save a calificacion.
     *
     * @param calificacion the entity to save.
     * @return the persisted entity.
     */
    Calificacion save(Calificacion calificacion);

    /**
     * Updates a calificacion.
     *
     * @param calificacion the entity to update.
     * @return the persisted entity.
     */
    Calificacion update(Calificacion calificacion);

    /**
     * Partially updates a calificacion.
     *
     * @param calificacion the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Calificacion> partialUpdate(Calificacion calificacion);

    /**
     * Get all the calificacions.
     *
     * @return the list of entities.
     */
    List<Calificacion> findAll();

    /**
     * Get all the calificacions with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Calificacion> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" calificacion.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Calificacion> findOne(Long id);

    /**
     * Delete the "id" calificacion.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
