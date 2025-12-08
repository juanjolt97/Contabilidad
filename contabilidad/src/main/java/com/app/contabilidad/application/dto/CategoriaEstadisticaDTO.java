package com.app.contabilidad.application.dto;

import java.math.BigDecimal;

public class CategoriaEstadisticaDTO {
    private String categoria;
    private BigDecimal total;
    private double porcentaje;

    public CategoriaEstadisticaDTO() {
    }

    public CategoriaEstadisticaDTO(String categoria, BigDecimal total, double porcentaje) {
        this.categoria = categoria;
        this.total = total;
        this.porcentaje = porcentaje;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public double getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(double porcentaje) {
        this.porcentaje = porcentaje;
    }
}
