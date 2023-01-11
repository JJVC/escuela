package com.demo.escuela.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.demo.escuela.IntegrationTest;
import com.demo.escuela.domain.Alumno;
import com.demo.escuela.repository.AlumnoRepository;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link AlumnoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AlumnoResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_AP_PATERNO = "AAAAAAAAAA";
    private static final String UPDATED_AP_PATERNO = "BBBBBBBBBB";

    private static final String DEFAULT_AP_MATERNO = "AAAAAAAAAA";
    private static final String UPDATED_AP_MATERNO = "BBBBBBBBBB";

    private static final String DEFAULT_DISPLAY = "AAAAAAAAAA";
    private static final String UPDATED_DISPLAY = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ACTIVO = false;
    private static final Boolean UPDATED_ACTIVO = true;

    private static final String ENTITY_API_URL = "/api/alumnos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AlumnoRepository alumnoRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAlumnoMockMvc;

    private Alumno alumno;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Alumno createEntity(EntityManager em) {
        Alumno alumno = new Alumno()
            .nombre(DEFAULT_NOMBRE)
            .apPaterno(DEFAULT_AP_PATERNO)
            .apMaterno(DEFAULT_AP_MATERNO)
            .display(DEFAULT_DISPLAY)
            .activo(DEFAULT_ACTIVO);
        return alumno;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Alumno createUpdatedEntity(EntityManager em) {
        Alumno alumno = new Alumno()
            .nombre(UPDATED_NOMBRE)
            .apPaterno(UPDATED_AP_PATERNO)
            .apMaterno(UPDATED_AP_MATERNO)
            .display(UPDATED_DISPLAY)
            .activo(UPDATED_ACTIVO);
        return alumno;
    }

    @BeforeEach
    public void initTest() {
        alumno = createEntity(em);
    }

    @Test
    @Transactional
    void createAlumno() throws Exception {
        int databaseSizeBeforeCreate = alumnoRepository.findAll().size();
        // Create the Alumno
        restAlumnoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(alumno)))
            .andExpect(status().isCreated());

        // Validate the Alumno in the database
        List<Alumno> alumnoList = alumnoRepository.findAll();
        assertThat(alumnoList).hasSize(databaseSizeBeforeCreate + 1);
        Alumno testAlumno = alumnoList.get(alumnoList.size() - 1);
        assertThat(testAlumno.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testAlumno.getApPaterno()).isEqualTo(DEFAULT_AP_PATERNO);
        assertThat(testAlumno.getApMaterno()).isEqualTo(DEFAULT_AP_MATERNO);
        assertThat(testAlumno.getDisplay()).isEqualTo(DEFAULT_DISPLAY);
        assertThat(testAlumno.getActivo()).isEqualTo(DEFAULT_ACTIVO);
    }

    @Test
    @Transactional
    void createAlumnoWithExistingId() throws Exception {
        // Create the Alumno with an existing ID
        alumno.setId(1L);

        int databaseSizeBeforeCreate = alumnoRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAlumnoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(alumno)))
            .andExpect(status().isBadRequest());

        // Validate the Alumno in the database
        List<Alumno> alumnoList = alumnoRepository.findAll();
        assertThat(alumnoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNombreIsRequired() throws Exception {
        int databaseSizeBeforeTest = alumnoRepository.findAll().size();
        // set the field null
        alumno.setNombre(null);

        // Create the Alumno, which fails.

        restAlumnoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(alumno)))
            .andExpect(status().isBadRequest());

        List<Alumno> alumnoList = alumnoRepository.findAll();
        assertThat(alumnoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkApPaternoIsRequired() throws Exception {
        int databaseSizeBeforeTest = alumnoRepository.findAll().size();
        // set the field null
        alumno.setApPaterno(null);

        // Create the Alumno, which fails.

        restAlumnoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(alumno)))
            .andExpect(status().isBadRequest());

        List<Alumno> alumnoList = alumnoRepository.findAll();
        assertThat(alumnoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAlumnos() throws Exception {
        // Initialize the database
        alumnoRepository.saveAndFlush(alumno);

        // Get all the alumnoList
        restAlumnoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alumno.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].apPaterno").value(hasItem(DEFAULT_AP_PATERNO)))
            .andExpect(jsonPath("$.[*].apMaterno").value(hasItem(DEFAULT_AP_MATERNO)))
            .andExpect(jsonPath("$.[*].display").value(hasItem(DEFAULT_DISPLAY)))
            .andExpect(jsonPath("$.[*].activo").value(hasItem(DEFAULT_ACTIVO.booleanValue())));
    }

    @Test
    @Transactional
    void getAlumno() throws Exception {
        // Initialize the database
        alumnoRepository.saveAndFlush(alumno);

        // Get the alumno
        restAlumnoMockMvc
            .perform(get(ENTITY_API_URL_ID, alumno.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(alumno.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.apPaterno").value(DEFAULT_AP_PATERNO))
            .andExpect(jsonPath("$.apMaterno").value(DEFAULT_AP_MATERNO))
            .andExpect(jsonPath("$.display").value(DEFAULT_DISPLAY))
            .andExpect(jsonPath("$.activo").value(DEFAULT_ACTIVO.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingAlumno() throws Exception {
        // Get the alumno
        restAlumnoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAlumno() throws Exception {
        // Initialize the database
        alumnoRepository.saveAndFlush(alumno);

        int databaseSizeBeforeUpdate = alumnoRepository.findAll().size();

        // Update the alumno
        Alumno updatedAlumno = alumnoRepository.findById(alumno.getId()).get();
        // Disconnect from session so that the updates on updatedAlumno are not directly saved in db
        em.detach(updatedAlumno);
        updatedAlumno
            .nombre(UPDATED_NOMBRE)
            .apPaterno(UPDATED_AP_PATERNO)
            .apMaterno(UPDATED_AP_MATERNO)
            .display(UPDATED_DISPLAY)
            .activo(UPDATED_ACTIVO);

        restAlumnoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAlumno.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedAlumno))
            )
            .andExpect(status().isOk());

        // Validate the Alumno in the database
        List<Alumno> alumnoList = alumnoRepository.findAll();
        assertThat(alumnoList).hasSize(databaseSizeBeforeUpdate);
        Alumno testAlumno = alumnoList.get(alumnoList.size() - 1);
        assertThat(testAlumno.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testAlumno.getApPaterno()).isEqualTo(UPDATED_AP_PATERNO);
        assertThat(testAlumno.getApMaterno()).isEqualTo(UPDATED_AP_MATERNO);
        assertThat(testAlumno.getDisplay()).isEqualTo(UPDATED_DISPLAY);
        assertThat(testAlumno.getActivo()).isEqualTo(UPDATED_ACTIVO);
    }

    @Test
    @Transactional
    void putNonExistingAlumno() throws Exception {
        int databaseSizeBeforeUpdate = alumnoRepository.findAll().size();
        alumno.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlumnoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, alumno.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(alumno))
            )
            .andExpect(status().isBadRequest());

        // Validate the Alumno in the database
        List<Alumno> alumnoList = alumnoRepository.findAll();
        assertThat(alumnoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAlumno() throws Exception {
        int databaseSizeBeforeUpdate = alumnoRepository.findAll().size();
        alumno.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlumnoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(alumno))
            )
            .andExpect(status().isBadRequest());

        // Validate the Alumno in the database
        List<Alumno> alumnoList = alumnoRepository.findAll();
        assertThat(alumnoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAlumno() throws Exception {
        int databaseSizeBeforeUpdate = alumnoRepository.findAll().size();
        alumno.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlumnoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(alumno)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Alumno in the database
        List<Alumno> alumnoList = alumnoRepository.findAll();
        assertThat(alumnoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAlumnoWithPatch() throws Exception {
        // Initialize the database
        alumnoRepository.saveAndFlush(alumno);

        int databaseSizeBeforeUpdate = alumnoRepository.findAll().size();

        // Update the alumno using partial update
        Alumno partialUpdatedAlumno = new Alumno();
        partialUpdatedAlumno.setId(alumno.getId());

        partialUpdatedAlumno.apPaterno(UPDATED_AP_PATERNO).display(UPDATED_DISPLAY).activo(UPDATED_ACTIVO);

        restAlumnoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlumno.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAlumno))
            )
            .andExpect(status().isOk());

        // Validate the Alumno in the database
        List<Alumno> alumnoList = alumnoRepository.findAll();
        assertThat(alumnoList).hasSize(databaseSizeBeforeUpdate);
        Alumno testAlumno = alumnoList.get(alumnoList.size() - 1);
        assertThat(testAlumno.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testAlumno.getApPaterno()).isEqualTo(UPDATED_AP_PATERNO);
        assertThat(testAlumno.getApMaterno()).isEqualTo(DEFAULT_AP_MATERNO);
        assertThat(testAlumno.getDisplay()).isEqualTo(UPDATED_DISPLAY);
        assertThat(testAlumno.getActivo()).isEqualTo(UPDATED_ACTIVO);
    }

    @Test
    @Transactional
    void fullUpdateAlumnoWithPatch() throws Exception {
        // Initialize the database
        alumnoRepository.saveAndFlush(alumno);

        int databaseSizeBeforeUpdate = alumnoRepository.findAll().size();

        // Update the alumno using partial update
        Alumno partialUpdatedAlumno = new Alumno();
        partialUpdatedAlumno.setId(alumno.getId());

        partialUpdatedAlumno
            .nombre(UPDATED_NOMBRE)
            .apPaterno(UPDATED_AP_PATERNO)
            .apMaterno(UPDATED_AP_MATERNO)
            .display(UPDATED_DISPLAY)
            .activo(UPDATED_ACTIVO);

        restAlumnoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlumno.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAlumno))
            )
            .andExpect(status().isOk());

        // Validate the Alumno in the database
        List<Alumno> alumnoList = alumnoRepository.findAll();
        assertThat(alumnoList).hasSize(databaseSizeBeforeUpdate);
        Alumno testAlumno = alumnoList.get(alumnoList.size() - 1);
        assertThat(testAlumno.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testAlumno.getApPaterno()).isEqualTo(UPDATED_AP_PATERNO);
        assertThat(testAlumno.getApMaterno()).isEqualTo(UPDATED_AP_MATERNO);
        assertThat(testAlumno.getDisplay()).isEqualTo(UPDATED_DISPLAY);
        assertThat(testAlumno.getActivo()).isEqualTo(UPDATED_ACTIVO);
    }

    @Test
    @Transactional
    void patchNonExistingAlumno() throws Exception {
        int databaseSizeBeforeUpdate = alumnoRepository.findAll().size();
        alumno.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlumnoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, alumno.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(alumno))
            )
            .andExpect(status().isBadRequest());

        // Validate the Alumno in the database
        List<Alumno> alumnoList = alumnoRepository.findAll();
        assertThat(alumnoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAlumno() throws Exception {
        int databaseSizeBeforeUpdate = alumnoRepository.findAll().size();
        alumno.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlumnoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(alumno))
            )
            .andExpect(status().isBadRequest());

        // Validate the Alumno in the database
        List<Alumno> alumnoList = alumnoRepository.findAll();
        assertThat(alumnoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAlumno() throws Exception {
        int databaseSizeBeforeUpdate = alumnoRepository.findAll().size();
        alumno.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlumnoMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(alumno)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Alumno in the database
        List<Alumno> alumnoList = alumnoRepository.findAll();
        assertThat(alumnoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAlumno() throws Exception {
        // Initialize the database
        alumnoRepository.saveAndFlush(alumno);

        int databaseSizeBeforeDelete = alumnoRepository.findAll().size();

        // Delete the alumno
        restAlumnoMockMvc
            .perform(delete(ENTITY_API_URL_ID, alumno.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Alumno> alumnoList = alumnoRepository.findAll();
        assertThat(alumnoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
