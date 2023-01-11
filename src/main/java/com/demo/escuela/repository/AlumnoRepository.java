package com.demo.escuela.repository;

import com.demo.escuela.domain.Alumno;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Alumno entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AlumnoRepository extends JpaRepository<Alumno, Long> {}
