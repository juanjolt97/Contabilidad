package com.app.contabilidad.infrastructure.adapters.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository de Spring Data JPA para la entidad MovimientoEntity
 */
@Repository
public interface MovimientoJpaRepository extends JpaRepository<MovimientoEntity, Long> {
    /**
     * Busca movimientos por tipo
     */
    List<MovimientoEntity> findByTipo(MovimientoEntity.TipoMovimiento tipo);

    /**
     * Busca movimientos por categoría
     */
    List<MovimientoEntity> findByCategoria(String categoria);
}
