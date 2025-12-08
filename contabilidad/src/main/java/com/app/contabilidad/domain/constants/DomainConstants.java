package com.app.contabilidad.domain.constants;

/**
 * Constantes de la capa de dominio
 * Contiene mensajes, validaciones y valores del negocio
 */
public final class DomainConstants {
    private DomainConstants() {
        throw new AssertionError("No se puede instanciar DomainConstants");
    }

    // Validaciones
    public static final String MOVIMIENTO_NO_VALIDO = "El movimiento no es válido";
    public static final String CANTIDAD_REQUERIDA = "La cantidad es requerida";
    public static final String DESCRIPCION_REQUERIDA = "La descripción es requerida";
    public static final String CATEGORIA_REQUERIDA = "La categoría es requerida";
    public static final String TIPO_REQUERIDO = "El tipo de movimiento es requerido";
    public static final String FECHA_REQUERIDA = "La fecha es requerida";

    // Mensajes de éxito
    public static final String MOVIMIENTO_CREADO = "Movimiento creado exitosamente";
    public static final String MOVIMIENTO_ACTUALIZADO = "Movimiento actualizado exitosamente";
    public static final String MOVIMIENTO_ELIMINADO = "Movimiento eliminado exitosamente";

    // Mensajes de error
    public static final String ERROR_CREAR_MOVIMIENTO = "Error al crear el movimiento: ";
    public static final String ERROR_ACTUALIZAR_MOVIMIENTO = "Error al actualizar el movimiento: ";
    public static final String ERROR_ELIMINAR_MOVIMIENTO = "Error al eliminar el movimiento: ";

    // Textos de categorías
    public static final String CATEGORIA_ALIMENTACION = "Alimentación";
    public static final String CATEGORIA_TRANSPORTE = "Transporte";
    public static final String CATEGORIA_SERVICIOS = "Servicios";
    public static final String CATEGORIA_SALUD = "Salud";
    public static final String CATEGORIA_EDUCACION = "Educación";
    public static final String CATEGORIA_ENTRETENIMIENTO = "Entretenimiento";
    public static final String CATEGORIA_HOGAR = "Hogar";
    public static final String CATEGORIA_OTROS = "Otros";

    // Símbolos para tipos
    public static final String TIPO_GASTO_SYMBOL = "📉 Gasto";
    public static final String TIPO_BENEFICIO_SYMBOL = "📈 Beneficio";
}
