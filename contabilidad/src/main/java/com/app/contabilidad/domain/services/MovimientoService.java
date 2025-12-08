package com.app.contabilidad.domain.services;

import com.app.contabilidad.domain.entities.Movimiento;
import com.app.contabilidad.domain.ports.MovimientoRepositoryPort;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Servicio de dominio que contiene la lógica de negocio relacionada con movimientos.
 * Parte de la capa de dominio de la arquitectura hexagonal.
 */
public class MovimientoService {
    private final MovimientoRepositoryPort movimientoRepository;

    public MovimientoService(MovimientoRepositoryPort movimientoRepository) {
        this.movimientoRepository = movimientoRepository;
    }

    /**
     * Crea un nuevo movimiento validando que sea válido
     */
    public Movimiento crearMovimiento(Movimiento movimiento) {
        if (!movimiento.esValido()) {
            throw new IllegalArgumentException("El movimiento no es válido");
        }
        return movimientoRepository.guardar(movimiento);
    }

    /**
     * Obtiene el total de gastos
     */
    public BigDecimal calcularTotalGastos() {
        return movimientoRepository.obtenerPorTipo(Movimiento.TipoMovimiento.GASTO)
                .stream()
                .map(Movimiento::getCantidad)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Obtiene el total de beneficios
     */
    public BigDecimal calcularTotalBeneficios() {
        return movimientoRepository.obtenerPorTipo(Movimiento.TipoMovimiento.BENEFICIO)
                .stream()
                .map(Movimiento::getCantidad)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Calcula el balance neto (beneficios - gastos)
     */
    public BigDecimal calcularBalance() {
        return calcularTotalBeneficios().subtract(calcularTotalGastos());
    }

    /**
     * Obtiene todos los movimientos
     */
    public List<Movimiento> obtenerTodosLosMovimientos() {
        return movimientoRepository.obtenerTodos();
    }

    /**
     * Obtiene un movimiento por ID
     */
    public Optional<Movimiento> obtenerMovimiento(Long id) {
        return movimientoRepository.obtenerPorId(id);
    }

    /**
     * Obtiene movimientos por categoría
     */
    public List<Movimiento> obtenerMovimientosPorCategoria(String categoria) {
        return movimientoRepository.obtenerPorCategoria(categoria);
    }

    /**
     * Actualiza un movimiento
     */
    public Movimiento actualizarMovimiento(Movimiento movimiento) {
        if (!movimiento.esValido()) {
            throw new IllegalArgumentException("El movimiento no es válido");
        }
        return movimientoRepository.actualizar(movimiento);
    }

    /**
     * Elimina un movimiento
     */
    public void eliminarMovimiento(Long id) {
        movimientoRepository.eliminar(id);
    }
}
