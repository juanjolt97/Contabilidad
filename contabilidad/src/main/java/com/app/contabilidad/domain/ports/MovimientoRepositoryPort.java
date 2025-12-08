package com.app.contabilidad.domain.ports;

import com.app.contabilidad.domain.entities.Movimiento;

import java.util.List;
import java.util.Optional;

/**
 * Puerto (interfaz) que define el contrato para persistencia de movimientos.
 * Parte de la capa de dominio de la arquitectura hexagonal.
 */
public interface MovimientoRepositoryPort {
    /**
     * Guarda un movimiento
     */
    Movimiento guardar(Movimiento movimiento);

    /**
     * Obtiene un movimiento por su ID
     */
    Optional<Movimiento> obtenerPorId(Long id);

    /**
     * Obtiene todos los movimientos
     */
    List<Movimiento> obtenerTodos();

    /**
     * Actualiza un movimiento
     */
    Movimiento actualizar(Movimiento movimiento);

    /**
     * Elimina un movimiento
     */
    void eliminar(Long id);

    /**
     * Obtiene movimientos por tipo
     */
    List<Movimiento> obtenerPorTipo(Movimiento.TipoMovimiento tipo);

    /**
     * Obtiene movimientos por categoría
     */
    List<Movimiento> obtenerPorCategoria(String categoria);
}
