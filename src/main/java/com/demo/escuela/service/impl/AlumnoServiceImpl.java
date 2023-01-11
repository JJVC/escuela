package com.demo.escuela.service.impl;

import com.demo.escuela.domain.Alumno;
import com.demo.escuela.repository.AlumnoRepository;
import com.demo.escuela.service.AlumnoService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Alumno}.
 */
@Service
@Transactional
public class AlumnoServiceImpl implements AlumnoService {

    private final Logger log = LoggerFactory.getLogger(AlumnoServiceImpl.class);

    private final AlumnoRepository alumnoRepository;

    public AlumnoServiceImpl(AlumnoRepository alumnoRepository) {
        this.alumnoRepository = alumnoRepository;
    }

    @Override
    public Alumno save(Alumno alumno) {
        log.debug("Request to save Alumno : {}", alumno);
        return alumnoRepository.save(alumno);
    }

    @Override
    public Alumno update(Alumno alumno) {
        log.debug("Request to update Alumno : {}", alumno);
        return alumnoRepository.save(alumno);
    }

    @Override
    public Optional<Alumno> partialUpdate(Alumno alumno) {
        log.debug("Request to partially update Alumno : {}", alumno);

        return alumnoRepository
            .findById(alumno.getId())
            .map(existingAlumno -> {
                if (alumno.getNombre() != null) {
                    existingAlumno.setNombre(alumno.getNombre());
                }
                if (alumno.getApPaterno() != null) {
                    existingAlumno.setApPaterno(alumno.getApPaterno());
                }
                if (alumno.getApMaterno() != null) {
                    existingAlumno.setApMaterno(alumno.getApMaterno());
                }
                if (alumno.getDisplay() != null) {
                    existingAlumno.setDisplay(alumno.getDisplay());
                }
                if (alumno.getActivo() != null) {
                    existingAlumno.setActivo(alumno.getActivo());
                }

                return existingAlumno;
            })
            .map(alumnoRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Alumno> findAll() {
        log.debug("Request to get all Alumnos");
        return alumnoRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Alumno> findOne(Long id) {
        log.debug("Request to get Alumno : {}", id);
        return alumnoRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Alumno : {}", id);
        alumnoRepository.deleteById(id);
    }
}
