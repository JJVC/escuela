package com.demo.escuela.web.rest;

import com.demo.escuela.domain.Alumno;
import com.demo.escuela.repository.AlumnoRepository;
import com.demo.escuela.service.AlumnoService;
import com.demo.escuela.service.dto.AlumnoDTO;
import com.demo.escuela.web.rest.errors.BadRequestAlertException;
import java.math.RoundingMode;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.demo.escuela.domain.Alumno}.
 */
@RestController
@RequestMapping("/api")
public class AlumnoResource {

    private final Logger log = LoggerFactory.getLogger(AlumnoResource.class);

    private static final String ENTITY_NAME = "alumno";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AlumnoService alumnoService;

    private final AlumnoRepository alumnoRepository;

    public AlumnoResource(AlumnoService alumnoService, AlumnoRepository alumnoRepository) {
        this.alumnoService = alumnoService;
        this.alumnoRepository = alumnoRepository;
    }

    /**
     * {@code POST  /alumnos} : Create a new alumno.
     *
     * @param alumno the alumno to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new alumno, or with status {@code 400 (Bad Request)} if the alumno has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/alumnos")
    public ResponseEntity<Alumno> createAlumno(@Valid @RequestBody Alumno alumno) throws URISyntaxException {
        log.debug("REST request to save Alumno : {}", alumno);
        if (alumno.getId() != null) {
            throw new BadRequestAlertException("A new alumno cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Alumno result = alumnoService.save(alumno);
        return ResponseEntity
            .created(new URI("/api/alumnos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /alumnos/:id} : Updates an existing alumno.
     *
     * @param id the id of the alumno to save.
     * @param alumno the alumno to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alumno,
     * or with status {@code 400 (Bad Request)} if the alumno is not valid,
     * or with status {@code 500 (Internal Server Error)} if the alumno couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/alumnos/{id}")
    public ResponseEntity<Alumno> updateAlumno(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Alumno alumno
    ) throws URISyntaxException {
        log.debug("REST request to update Alumno : {}, {}", id, alumno);
        if (alumno.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alumno.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alumnoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Alumno result = alumnoService.update(alumno);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alumno.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /alumnos/:id} : Partial updates given fields of an existing alumno, field will ignore if it is null
     *
     * @param id the id of the alumno to save.
     * @param alumno the alumno to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alumno,
     * or with status {@code 400 (Bad Request)} if the alumno is not valid,
     * or with status {@code 404 (Not Found)} if the alumno is not found,
     * or with status {@code 500 (Internal Server Error)} if the alumno couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/alumnos/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Alumno> partialUpdateAlumno(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Alumno alumno
    ) throws URISyntaxException {
        log.debug("REST request to partial update Alumno partially : {}, {}", id, alumno);
        if (alumno.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alumno.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alumnoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Alumno> result = alumnoService.partialUpdate(alumno);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alumno.getId().toString())
        );
    }

    /**
     * {@code GET  /alumnos} : get all the alumnos.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of alumnos in body.
     */
    @GetMapping("/alumnos")
    public List<Alumno> getAllAlumnos() {
        log.debug("REST request to get all Alumnos");
        return alumnoService.findAll();
    }

    /**
     * {@code GET  /alumnos/:id} : get the "id" alumno.
     *
     * @param id the id of the alumno to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the alumno, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/alumnos/{id}")
    public ResponseEntity<Alumno> getAlumno(@PathVariable Long id) {
        log.debug("REST request to get Alumno : {}", id);
        Optional<Alumno> alumno = alumnoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(alumno);
    }

    @GetMapping("/alumnos/{id}/calificaciones")
    public ResponseEntity<AlumnoDTO> getAlumnoCalificaciones(@PathVariable Long id) {
        log.debug("REST request to get Alumno + Calificaciones : {}", id);
        Optional<Alumno> alumno = alumnoService.findOneWithEagerRelationships(id);
        Optional<AlumnoDTO> alumnoDTO = Optional.empty();
        if (alumno.isPresent()) {
            Double promedio = alumno.get().getCalificacions().stream().mapToDouble(value -> value.getCalificacion()).average().orElse(0);
            DecimalFormat df = new DecimalFormat("#.##");
            df.setRoundingMode(RoundingMode.DOWN);

            alumnoDTO = Optional.of(new AlumnoDTO(alumno.get()));
            alumnoDTO.get().setPromedio(Float.valueOf(df.format(promedio)));
        }
        return ResponseUtil.wrapOrNotFound(alumnoDTO);
    }

    /**
     * {@code DELETE  /alumnos/:id} : delete the "id" alumno.
     *
     * @param id the id of the alumno to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/alumnos/{id}")
    public ResponseEntity<Void> deleteAlumno(@PathVariable Long id) {
        log.debug("REST request to delete Alumno : {}", id);
        alumnoService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
