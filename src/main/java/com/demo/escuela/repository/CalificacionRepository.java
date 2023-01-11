package com.demo.escuela.repository;

import com.demo.escuela.domain.Calificacion;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Calificacion entity.
 */
@Repository
public interface CalificacionRepository extends JpaRepository<Calificacion, Long> {
    default Optional<Calificacion> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Calificacion> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Calificacion> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct calificacion from Calificacion calificacion left join fetch calificacion.materia left join fetch calificacion.alumno",
        countQuery = "select count(distinct calificacion) from Calificacion calificacion"
    )
    Page<Calificacion> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select distinct calificacion from Calificacion calificacion left join fetch calificacion.materia left join fetch calificacion.alumno"
    )
    List<Calificacion> findAllWithToOneRelationships();

    @Query(
        "select calificacion from Calificacion calificacion left join fetch calificacion.materia left join fetch calificacion.alumno where calificacion.id =:id"
    )
    Optional<Calificacion> findOneWithToOneRelationships(@Param("id") Long id);
}
