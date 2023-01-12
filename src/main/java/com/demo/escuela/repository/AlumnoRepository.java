package com.demo.escuela.repository;

import com.demo.escuela.domain.Alumno;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Alumno entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AlumnoRepository extends JpaRepository<Alumno, Long> {
    default Optional<Alumno> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    @Query("select alumno from Alumno alumno left join fetch alumno.calificacions c left join fetch c.materia where alumno.id =:id")
    Optional<Alumno> findOneWithToOneRelationships(@Param("id") Long id);
}
