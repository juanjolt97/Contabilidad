package com.app.contabilidad.infrastructure.adapters.persistence;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Entidad JPA que mapea la tabla de movimientos en la base de datos
 * Adaptador de persistencia de la arquitectura hexagonal
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "movimientos")
public class MovimientoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String descripcion;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal cantidad;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoMovimiento tipo;

    @Column(nullable = false)
    private LocalDate fecha;

    @Column(nullable = false)
    private String categoria;

    @Column(columnDefinition = "TEXT")
    private String notas;

    public enum TipoMovimiento {
        GASTO, BENEFICIO
    }
}
