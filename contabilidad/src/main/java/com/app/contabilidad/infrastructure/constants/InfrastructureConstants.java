package com.app.contabilidad.infrastructure.constants;

/**
 * Constantes de la capa de infraestructura
 * Contiene rutas, nombres de vistas, endpoints y configuraciones de acceso
 */
public final class InfrastructureConstants {
    private InfrastructureConstants() {
        throw new AssertionError("No se puede instanciar InfrastructureConstants");
    }

    // Rutas base
    public static final String BASE_PATH = "/movimientos";
    public static final String API_BASE_PATH = "/movimientos/api";

    // Endpoints GET
    public static final String ENDPOINT_LISTAR = "";
    public static final String ENDPOINT_NUEVO = "/nuevo";
    public static final String ENDPOINT_EDITAR = "/{id}/editar";
    public static final String ENDPOINT_ELIMINAR = "/{id}/eliminar";
    public static final String ENDPOINT_CATEGORIA = "/categoria/{categoria}";
    public static final String ENDPOINT_ESTADISTICAS = "/estadisticas";
    public static final String ENDPOINT_RESUMEN_MENSUAL = "/resumen";
    public static final String ENDPOINT_INICIO = "/inicio";

    // Endpoints API
    public static final String API_ENDPOINT_ESTADISTICAS = "/api/estadisticas";
    public static final String API_ENDPOINT_RESUMEN_MENSUAL = "/api/resumen";

    // Nombres de vistas (templates)
    public static final String VIEW_LISTA = "movimientos/lista";
    public static final String VIEW_FORMULARIO = "movimientos/formulario";
    public static final String VIEW_FORMULARIO_EDITAR = "movimientos/formulario-editar";
    public static final String VIEW_LISTA_CATEGORIA = "movimientos/lista-categoria";
    public static final String VIEW_ESTADISTICAS = "movimientos/estadisticas";
    public static final String VIEW_RESUMEN_MENSUAL = "movimientos/resumen-mensual";

    // Redirecciones
    public static final String REDIRECT_MOVIMIENTOS = "redirect:/movimientos";
    public static final String REDIRECT_INICIO = "redirect:/movimientos";

    // Parámetros de request
    public static final String PARAM_ID = "id";
    public static final String PARAM_CATEGORIA = "categoria";

    // Emojis y símbolos
    public static final String EMOJI_EDITAR = "✏️ Editar";
    public static final String EMOJI_ELIMINAR = "🗑️ Eliminar";
    public static final String EMOJI_RESUMEN = "📊 Resumen";
    public static final String EMOJI_ALIMENTACION = "🍔 Alimentación";
    public static final String EMOJI_TRANSPORTE = "🚗 Transporte";
    public static final String EMOJI_SERVICIOS = "🔧 Servicios";
    public static final String EMOJI_SALUD = "⚕️ Salud";
    public static final String EMOJI_EDUCACION = "📚 Educación";
    public static final String EMOJI_ENTRETENIMIENTO = "🎬 Entretenimiento";
    public static final String EMOJI_HOGAR = "🏠 Hogar";
    public static final String EMOJI_OTROS = "📋 Otros";

    // Mensajes de confirmación
    public static final String CONFIRM_ELIMINAR = "¿Seguro que deseas eliminar este movimiento?";
}
