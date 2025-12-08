package com.app.contabilidad.domain.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Entidad de dominio que representa un movimiento (gasto o beneficio) del hogar.
 * Parte de la capa de dominio de la arquitectura hexagonal.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Movimiento {
    private Long id;
    private String descripcion;
    private BigDecimal cantidad;
    private TipoMovimiento tipo;
    private LocalDate fecha;
    private String categoria;
    private String notas;

    public enum TipoMovimiento {
        GASTO, BENEFICIO
    }

    /**
     * Valida que el movimiento sea válido
     */
    public boolean esValido() {
        return descripcion != null && !descripcion.trim().isEmpty()
                && cantidad != null && cantidad.compareTo(BigDecimal.ZERO) > 0
                && tipo != null
                && fecha != null
                && categoria != null && !categoria.trim().isEmpty();
    }
}
