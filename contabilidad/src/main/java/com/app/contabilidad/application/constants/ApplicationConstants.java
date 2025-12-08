package com.app.contabilidad.application.constants;

/**
 * Constantes de la capa de aplicación
 * Contiene nombres de atributos, claves de DTO y valores de casos de uso
 */
public final class ApplicationConstants {
    private ApplicationConstants() {
        throw new AssertionError("No se puede instanciar ApplicationConstants");
    }

    // Atributos de modelo
    public static final String ATTR_MOVIMIENTOS = "movimientos";
    public static final String ATTR_MOVIMIENTO = "movimiento";
    public static final String ATTR_RESUMEN = "resumen";
    public static final String ATTR_NUEVO = "nuevo";
    public static final String ATTR_TIPOS_MOVIMIENTO = "tiposMovimiento";
    public static final String ATTR_CATEGORIAS = "categorias";
    public static final String ATTR_CATEGORIA_ACTUAL = "categoriaActual";
    public static final String ATTR_ID = "id";
    public static final String ATTR_MENSAJE = "mensaje";
    public static final String ATTR_ERROR = "error";

    // Propiedades del resumen
    public static final String RESUMEN_TOTAL_GASTOS = "totalGastos";
    public static final String RESUMEN_TOTAL_BENEFICIOS = "totalBeneficios";
    public static final String RESUMEN_BALANCE = "balance";
    public static final String RESUMEN_CANTIDAD_MOVIMIENTOS = "cantidadMovimientos";
    public static final String RESUMEN_CANTIDAD_GASTOS = "cantidadGastos";
    public static final String RESUMEN_CANTIDAD_BENEFICIOS = "cantidadBeneficios";

    // Propiedades del movimiento
    public static final String MOV_DESCRIPCION = "descripcion";
    public static final String MOV_CANTIDAD = "cantidad";
    public static final String MOV_TIPO = "tipo";
    public static final String MOV_FECHA = "fecha";
    public static final String MOV_CATEGORIA = "categoria";
    public static final String MOV_NOTAS = "notas";

    // Categorías
    public static final String[] CATEGORIAS_LISTA = {
            "Alimentación",
            "Transporte",
            "Servicios",
            "Salud",
            "Educación",
            "Entretenimiento",
            "Hogar",
            "Otros"
    };
}
