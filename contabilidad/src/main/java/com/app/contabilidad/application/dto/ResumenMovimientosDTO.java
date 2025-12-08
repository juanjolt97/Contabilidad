package com.app.contabilidad.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO para mostrar información de resumen de movimientos
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResumenMovimientosDTO {
    private BigDecimal totalGastos;
    private BigDecimal totalBeneficios;
    private BigDecimal balance;
    private Long cantidadMovimientos;
    private Long cantidadGastos;
    private Long cantidadBeneficios;
}
