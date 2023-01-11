package com.demo.escuela.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.demo.escuela.IntegrationTest;
import com.demo.escuela.domain.Calificacion;
import com.demo.escuela.repository.CalificacionRepository;
import com.demo.escuela.service.CalificacionService;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link CalificacionResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class CalificacionResourceIT {

    private static final Float DEFAULT_CALIFICACION = 1F;
    private static final Float UPDATED_CALIFICACION = 2F;

    private static final Instant DEFAULT_FECHA_REGISTRO = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA_REGISTRO = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/calificacions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CalificacionRepository calificacionRepository;

    @Mock
    private CalificacionRepository calificacionRepositoryMock;

    @Mock
    private CalificacionService calificacionServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCalificacionMockMvc;

    private Calificacion calificacion;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Calificacion createEntity(EntityManager em) {
        Calificacion calificacion = new Calificacion().calificacion(DEFAULT_CALIFICACION).fechaRegistro(DEFAULT_FECHA_REGISTRO);
        return calificacion;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Calificacion createUpdatedEntity(EntityManager em) {
        Calificacion calificacion = new Calificacion().calificacion(UPDATED_CALIFICACION).fechaRegistro(UPDATED_FECHA_REGISTRO);
        return calificacion;
    }

    @BeforeEach
    public void initTest() {
        calificacion = createEntity(em);
    }

    @Test
    @Transactional
    void createCalificacion() throws Exception {
        int databaseSizeBeforeCreate = calificacionRepository.findAll().size();
        // Create the Calificacion
        restCalificacionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(calificacion)))
            .andExpect(status().isCreated());

        // Validate the Calificacion in the database
        List<Calificacion> calificacionList = calificacionRepository.findAll();
        assertThat(calificacionList).hasSize(databaseSizeBeforeCreate + 1);
        Calificacion testCalificacion = calificacionList.get(calificacionList.size() - 1);
        assertThat(testCalificacion.getCalificacion()).isEqualTo(DEFAULT_CALIFICACION);
        assertThat(testCalificacion.getFechaRegistro()).isEqualTo(DEFAULT_FECHA_REGISTRO);
    }

    @Test
    @Transactional
    void createCalificacionWithExistingId() throws Exception {
        // Create the Calificacion with an existing ID
        calificacion.setId(1L);

        int databaseSizeBeforeCreate = calificacionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCalificacionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(calificacion)))
            .andExpect(status().isBadRequest());

        // Validate the Calificacion in the database
        List<Calificacion> calificacionList = calificacionRepository.findAll();
        assertThat(calificacionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCalificacionIsRequired() throws Exception {
        int databaseSizeBeforeTest = calificacionRepository.findAll().size();
        // set the field null
        calificacion.setCalificacion(null);

        // Create the Calificacion, which fails.

        restCalificacionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(calificacion)))
            .andExpect(status().isBadRequest());

        List<Calificacion> calificacionList = calificacionRepository.findAll();
        assertThat(calificacionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCalificacions() throws Exception {
        // Initialize the database
        calificacionRepository.saveAndFlush(calificacion);

        // Get all the calificacionList
        restCalificacionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(calificacion.getId().intValue())))
            .andExpect(jsonPath("$.[*].calificacion").value(hasItem(DEFAULT_CALIFICACION.doubleValue())))
            .andExpect(jsonPath("$.[*].fechaRegistro").value(hasItem(DEFAULT_FECHA_REGISTRO.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCalificacionsWithEagerRelationshipsIsEnabled() throws Exception {
        when(calificacionServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCalificacionMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(calificacionServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCalificacionsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(calificacionServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCalificacionMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(calificacionRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getCalificacion() throws Exception {
        // Initialize the database
        calificacionRepository.saveAndFlush(calificacion);

        // Get the calificacion
        restCalificacionMockMvc
            .perform(get(ENTITY_API_URL_ID, calificacion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(calificacion.getId().intValue()))
            .andExpect(jsonPath("$.calificacion").value(DEFAULT_CALIFICACION.doubleValue()))
            .andExpect(jsonPath("$.fechaRegistro").value(DEFAULT_FECHA_REGISTRO.toString()));
    }

    @Test
    @Transactional
    void getNonExistingCalificacion() throws Exception {
        // Get the calificacion
        restCalificacionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCalificacion() throws Exception {
        // Initialize the database
        calificacionRepository.saveAndFlush(calificacion);

        int databaseSizeBeforeUpdate = calificacionRepository.findAll().size();

        // Update the calificacion
        Calificacion updatedCalificacion = calificacionRepository.findById(calificacion.getId()).get();
        // Disconnect from session so that the updates on updatedCalificacion are not directly saved in db
        em.detach(updatedCalificacion);
        updatedCalificacion.calificacion(UPDATED_CALIFICACION).fechaRegistro(UPDATED_FECHA_REGISTRO);

        restCalificacionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCalificacion.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCalificacion))
            )
            .andExpect(status().isOk());

        // Validate the Calificacion in the database
        List<Calificacion> calificacionList = calificacionRepository.findAll();
        assertThat(calificacionList).hasSize(databaseSizeBeforeUpdate);
        Calificacion testCalificacion = calificacionList.get(calificacionList.size() - 1);
        assertThat(testCalificacion.getCalificacion()).isEqualTo(UPDATED_CALIFICACION);
        assertThat(testCalificacion.getFechaRegistro()).isEqualTo(UPDATED_FECHA_REGISTRO);
    }

    @Test
    @Transactional
    void putNonExistingCalificacion() throws Exception {
        int databaseSizeBeforeUpdate = calificacionRepository.findAll().size();
        calificacion.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCalificacionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, calificacion.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(calificacion))
            )
            .andExpect(status().isBadRequest());

        // Validate the Calificacion in the database
        List<Calificacion> calificacionList = calificacionRepository.findAll();
        assertThat(calificacionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCalificacion() throws Exception {
        int databaseSizeBeforeUpdate = calificacionRepository.findAll().size();
        calificacion.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCalificacionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(calificacion))
            )
            .andExpect(status().isBadRequest());

        // Validate the Calificacion in the database
        List<Calificacion> calificacionList = calificacionRepository.findAll();
        assertThat(calificacionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCalificacion() throws Exception {
        int databaseSizeBeforeUpdate = calificacionRepository.findAll().size();
        calificacion.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCalificacionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(calificacion)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Calificacion in the database
        List<Calificacion> calificacionList = calificacionRepository.findAll();
        assertThat(calificacionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCalificacionWithPatch() throws Exception {
        // Initialize the database
        calificacionRepository.saveAndFlush(calificacion);

        int databaseSizeBeforeUpdate = calificacionRepository.findAll().size();

        // Update the calificacion using partial update
        Calificacion partialUpdatedCalificacion = new Calificacion();
        partialUpdatedCalificacion.setId(calificacion.getId());

        partialUpdatedCalificacion.calificacion(UPDATED_CALIFICACION).fechaRegistro(UPDATED_FECHA_REGISTRO);

        restCalificacionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCalificacion.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCalificacion))
            )
            .andExpect(status().isOk());

        // Validate the Calificacion in the database
        List<Calificacion> calificacionList = calificacionRepository.findAll();
        assertThat(calificacionList).hasSize(databaseSizeBeforeUpdate);
        Calificacion testCalificacion = calificacionList.get(calificacionList.size() - 1);
        assertThat(testCalificacion.getCalificacion()).isEqualTo(UPDATED_CALIFICACION);
        assertThat(testCalificacion.getFechaRegistro()).isEqualTo(UPDATED_FECHA_REGISTRO);
    }

    @Test
    @Transactional
    void fullUpdateCalificacionWithPatch() throws Exception {
        // Initialize the database
        calificacionRepository.saveAndFlush(calificacion);

        int databaseSizeBeforeUpdate = calificacionRepository.findAll().size();

        // Update the calificacion using partial update
        Calificacion partialUpdatedCalificacion = new Calificacion();
        partialUpdatedCalificacion.setId(calificacion.getId());

        partialUpdatedCalificacion.calificacion(UPDATED_CALIFICACION).fechaRegistro(UPDATED_FECHA_REGISTRO);

        restCalificacionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCalificacion.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCalificacion))
            )
            .andExpect(status().isOk());

        // Validate the Calificacion in the database
        List<Calificacion> calificacionList = calificacionRepository.findAll();
        assertThat(calificacionList).hasSize(databaseSizeBeforeUpdate);
        Calificacion testCalificacion = calificacionList.get(calificacionList.size() - 1);
        assertThat(testCalificacion.getCalificacion()).isEqualTo(UPDATED_CALIFICACION);
        assertThat(testCalificacion.getFechaRegistro()).isEqualTo(UPDATED_FECHA_REGISTRO);
    }

    @Test
    @Transactional
    void patchNonExistingCalificacion() throws Exception {
        int databaseSizeBeforeUpdate = calificacionRepository.findAll().size();
        calificacion.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCalificacionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, calificacion.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(calificacion))
            )
            .andExpect(status().isBadRequest());

        // Validate the Calificacion in the database
        List<Calificacion> calificacionList = calificacionRepository.findAll();
        assertThat(calificacionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCalificacion() throws Exception {
        int databaseSizeBeforeUpdate = calificacionRepository.findAll().size();
        calificacion.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCalificacionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(calificacion))
            )
            .andExpect(status().isBadRequest());

        // Validate the Calificacion in the database
        List<Calificacion> calificacionList = calificacionRepository.findAll();
        assertThat(calificacionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCalificacion() throws Exception {
        int databaseSizeBeforeUpdate = calificacionRepository.findAll().size();
        calificacion.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCalificacionMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(calificacion))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Calificacion in the database
        List<Calificacion> calificacionList = calificacionRepository.findAll();
        assertThat(calificacionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCalificacion() throws Exception {
        // Initialize the database
        calificacionRepository.saveAndFlush(calificacion);

        int databaseSizeBeforeDelete = calificacionRepository.findAll().size();

        // Delete the calificacion
        restCalificacionMockMvc
            .perform(delete(ENTITY_API_URL_ID, calificacion.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Calificacion> calificacionList = calificacionRepository.findAll();
        assertThat(calificacionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
