package com.app.contabilidad.application.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para el resumen mensual de gastos y beneficios
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResumenMensualDTO {
    private String mes; // Formato: "2025-01" para enero de 2025
    private String mesFormato; // Formato legible: "Enero 2025"
    private BigDecimal totalGastos;
    private BigDecimal totalBeneficios;
    private BigDecimal balance;
    private long cantidadGastos;
    private long cantidadBeneficios;
    private long totalMovimientos;
}
