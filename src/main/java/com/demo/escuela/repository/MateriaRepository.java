package com.demo.escuela.repository;

import com.demo.escuela.domain.Materia;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Materia entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MateriaRepository extends JpaRepository<Materia, Long> {}
