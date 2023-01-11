package com.demo.escuela.service.impl;

import com.demo.escuela.domain.Calificacion;
import com.demo.escuela.repository.CalificacionRepository;
import com.demo.escuela.service.CalificacionService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Calificacion}.
 */
@Service
@Transactional
public class CalificacionServiceImpl implements CalificacionService {

    private final Logger log = LoggerFactory.getLogger(CalificacionServiceImpl.class);

    private final CalificacionRepository calificacionRepository;

    public CalificacionServiceImpl(CalificacionRepository calificacionRepository) {
        this.calificacionRepository = calificacionRepository;
    }

    @Override
    public Calificacion save(Calificacion calificacion) {
        log.debug("Request to save Calificacion : {}", calificacion);
        return calificacionRepository.save(calificacion);
    }

    @Override
    public Calificacion update(Calificacion calificacion) {
        log.debug("Request to update Calificacion : {}", calificacion);
        return calificacionRepository.save(calificacion);
    }

    @Override
    public Optional<Calificacion> partialUpdate(Calificacion calificacion) {
        log.debug("Request to partially update Calificacion : {}", calificacion);

        return calificacionRepository
            .findById(calificacion.getId())
            .map(existingCalificacion -> {
                if (calificacion.getCalificacion() != null) {
                    existingCalificacion.setCalificacion(calificacion.getCalificacion());
                }
                if (calificacion.getFechaRegistro() != null) {
                    existingCalificacion.setFechaRegistro(calificacion.getFechaRegistro());
                }

                return existingCalificacion;
            })
            .map(calificacionRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Calificacion> findAll() {
        log.debug("Request to get all Calificacions");
        return calificacionRepository.findAll();
    }

    public Page<Calificacion> findAllWithEagerRelationships(Pageable pageable) {
        return calificacionRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Calificacion> findOne(Long id) {
        log.debug("Request to get Calificacion : {}", id);
        return calificacionRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Calificacion : {}", id);
        calificacionRepository.deleteById(id);
    }
}
