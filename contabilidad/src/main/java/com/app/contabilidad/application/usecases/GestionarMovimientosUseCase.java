package com.app.contabilidad.application.usecases;

import com.app.contabilidad.domain.entities.Movimiento;
import com.app.contabilidad.domain.services.MovimientoService;
import com.app.contabilidad.application.dto.CrearMovimientoDTO;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Caso de uso para gestionar movimientos (gastos y beneficios)
 */
public class GestionarMovimientosUseCase {
    private final MovimientoService movimientoService;

    public GestionarMovimientosUseCase(MovimientoService movimientoService) {
        this.movimientoService = movimientoService;
    }

    /**
     * Crea un nuevo movimiento
     */
    public Movimiento crearMovimiento(CrearMovimientoDTO dto) {
        Movimiento movimiento = Movimiento.builder()
                .descripcion(dto.getDescripcion())
                .cantidad(dto.getCantidad())
                .tipo(Movimiento.TipoMovimiento.valueOf(dto.getTipo().toUpperCase()))
                .fecha(dto.getFecha() != null ? dto.getFecha() : LocalDate.now())
                .categoria(dto.getCategoria())
                .notas(dto.getNotas())
                .build();

        return movimientoService.crearMovimiento(movimiento);
    }

    /**
     * Obtiene todos los movimientos
     */
    public List<Movimiento> listarMovimientos() {
        return movimientoService.obtenerTodosLosMovimientos();
    }

    /**
     * Obtiene un movimiento específico
     */
    public Optional<Movimiento> obtenerMovimiento(Long id) {
        return movimientoService.obtenerMovimiento(id);
    }

    /**
     * Actualiza un movimiento
     */
    public Movimiento actualizarMovimiento(Long id, CrearMovimientoDTO dto) {
        Movimiento movimiento = Movimiento.builder()
                .id(id)
                .descripcion(dto.getDescripcion())
                .cantidad(dto.getCantidad())
                .tipo(Movimiento.TipoMovimiento.valueOf(dto.getTipo().toUpperCase()))
                .fecha(dto.getFecha() != null ? dto.getFecha() : LocalDate.now())
                .categoria(dto.getCategoria())
                .notas(dto.getNotas())
                .build();

        return movimientoService.actualizarMovimiento(movimiento);
    }

    /**
     * Elimina un movimiento
     */
    public void eliminarMovimiento(Long id) {
        movimientoService.eliminarMovimiento(id);
    }

    /**
     * Obtiene movimientos por categoría
     */
    public List<Movimiento> obtenerMovimientosPorCategoria(String categoria) {
        return movimientoService.obtenerMovimientosPorCategoria(categoria);
    }

    /**
     * Calcula el total de gastos
     */
    public BigDecimal calcularTotalGastos() {
        return movimientoService.calcularTotalGastos();
    }

    /**
     * Calcula el total de beneficios
     */
    public BigDecimal calcularTotalBeneficios() {
        return movimientoService.calcularTotalBeneficios();
    }

    /**
     * Calcula el balance neto
     */
    public BigDecimal calcularBalance() {
        return movimientoService.calcularBalance();
    }

    /**
     * Devuelve totales por categoría para un tipo de movimiento (GASTO o BENEFICIO)
     */
    public java.util.Map<String, java.math.BigDecimal> obtenerTotalesPorCategoria(com.app.contabilidad.domain.entities.Movimiento.TipoMovimiento tipo) {
        return movimientoService.sumarPorCategoria(tipo);
    }
}
