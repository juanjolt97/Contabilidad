package com.app.contabilidad.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO para crear un nuevo movimiento
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CrearMovimientoDTO {
    private String descripcion;
    private BigDecimal cantidad;
    private String tipo; // "GASTO" o "BENEFICIO"
    private LocalDate fecha;
    private String categoria;
    private String notas;
}
