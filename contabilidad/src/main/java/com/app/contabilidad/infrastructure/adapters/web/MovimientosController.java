package com.app.contabilidad.infrastructure.adapters.web;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.app.contabilidad.application.constants.ApplicationConstants;
import com.app.contabilidad.application.dto.CrearMovimientoDTO;
import com.app.contabilidad.application.dto.ResumenMovimientosDTO;
import com.app.contabilidad.application.usecases.GestionarMovimientosUseCase;
import com.app.contabilidad.domain.constants.DomainConstants;
import com.app.contabilidad.domain.entities.Movimiento;
import com.app.contabilidad.infrastructure.constants.InfrastructureConstants;

import lombok.RequiredArgsConstructor;

/**
 * Controlador web que adapta las peticiones HTTP a los casos de uso
 * Adaptador web de la arquitectura hexagonal
 */
@Controller
@RequestMapping(InfrastructureConstants.BASE_PATH)
@RequiredArgsConstructor
public class MovimientosController {
    private final GestionarMovimientosUseCase gestionarMovimientosUseCase;

    /**
     * Muestra la página principal con el listado de movimientos
     */
    @GetMapping
    public String listarMovimientos(Model model) {
        List<Movimiento> movimientos = gestionarMovimientosUseCase.listarMovimientos();
        ResumenMovimientosDTO resumen = calcularResumen(movimientos);

        model.addAttribute(ApplicationConstants.ATTR_MOVIMIENTOS, movimientos);
        model.addAttribute(ApplicationConstants.ATTR_RESUMEN, resumen);
        model.addAttribute(ApplicationConstants.ATTR_NUEVO, new CrearMovimientoDTO());

        return InfrastructureConstants.VIEW_LISTA;
    }

    /**
     * Muestra el formulario para crear un nuevo movimiento
     */
    @GetMapping(InfrastructureConstants.ENDPOINT_NUEVO)
    public String mostrarFormularioCrear(Model model) {
        model.addAttribute(ApplicationConstants.ATTR_MOVIMIENTO, new CrearMovimientoDTO());
        model.addAttribute(ApplicationConstants.ATTR_TIPOS_MOVIMIENTO, Movimiento.TipoMovimiento.values());
        model.addAttribute(ApplicationConstants.ATTR_CATEGORIAS, obtenerCategorias());
        return InfrastructureConstants.VIEW_FORMULARIO;
    }

    /**
     * Crea un nuevo movimiento
     */
    @PostMapping
    public String crearMovimiento(@ModelAttribute CrearMovimientoDTO dto, RedirectAttributes redirectAttributes) {
        try {
            gestionarMovimientosUseCase.crearMovimiento(dto);
            redirectAttributes.addFlashAttribute(ApplicationConstants.ATTR_MENSAJE, DomainConstants.MOVIMIENTO_CREADO);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute(ApplicationConstants.ATTR_ERROR, DomainConstants.ERROR_CREAR_MOVIMIENTO + e.getMessage());
        }
        return InfrastructureConstants.REDIRECT_MOVIMIENTOS;
    }

    /**
     * Muestra el formulario para editar un movimiento
     */
    @GetMapping(InfrastructureConstants.ENDPOINT_EDITAR)
    public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
        var movimiento = gestionarMovimientosUseCase.obtenerMovimiento(id);

        if (movimiento.isEmpty()) {
            return InfrastructureConstants.REDIRECT_MOVIMIENTOS;
        }

        Movimiento m = movimiento.get();
        CrearMovimientoDTO dto = CrearMovimientoDTO.builder()
                .descripcion(m.getDescripcion())
                .cantidad(m.getCantidad())
                .tipo(m.getTipo().name())
                .fecha(m.getFecha())
                .categoria(m.getCategoria())
                .notas(m.getNotas())
                .build();

        model.addAttribute(ApplicationConstants.ATTR_ID, id);
        model.addAttribute(ApplicationConstants.ATTR_MOVIMIENTO, dto);
        model.addAttribute(ApplicationConstants.ATTR_TIPOS_MOVIMIENTO, Movimiento.TipoMovimiento.values());
        model.addAttribute(ApplicationConstants.ATTR_CATEGORIAS, obtenerCategorias());
        return InfrastructureConstants.VIEW_FORMULARIO_EDITAR;
    }

    /**
     * Actualiza un movimiento
     */
    @PostMapping(InfrastructureConstants.ENDPOINT_EDITAR)
    public String actualizarMovimiento(@PathVariable Long id, @ModelAttribute CrearMovimientoDTO dto, RedirectAttributes redirectAttributes) {
        try {
            gestionarMovimientosUseCase.actualizarMovimiento(id, dto);
            redirectAttributes.addFlashAttribute(ApplicationConstants.ATTR_MENSAJE, DomainConstants.MOVIMIENTO_ACTUALIZADO);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute(ApplicationConstants.ATTR_ERROR, DomainConstants.ERROR_ACTUALIZAR_MOVIMIENTO + e.getMessage());
        }
        return InfrastructureConstants.REDIRECT_MOVIMIENTOS;
    }

    /**
     * Elimina un movimiento
     */
    @GetMapping(InfrastructureConstants.ENDPOINT_ELIMINAR)
    public String eliminarMovimiento(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            gestionarMovimientosUseCase.eliminarMovimiento(id);
            redirectAttributes.addFlashAttribute(ApplicationConstants.ATTR_MENSAJE, DomainConstants.MOVIMIENTO_ELIMINADO);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute(ApplicationConstants.ATTR_ERROR, DomainConstants.ERROR_ELIMINAR_MOVIMIENTO + e.getMessage());
        }
        return InfrastructureConstants.REDIRECT_MOVIMIENTOS;
    }

    /**
     * Muestra los movimientos filtrados por categoría
     */
    @GetMapping(InfrastructureConstants.ENDPOINT_CATEGORIA)
    public String listarPorCategoria(@PathVariable String categoria, Model model) {
        List<Movimiento> movimientos = gestionarMovimientosUseCase.obtenerMovimientosPorCategoria(categoria);
        ResumenMovimientosDTO resumen = calcularResumen(movimientos);

        model.addAttribute(ApplicationConstants.ATTR_MOVIMIENTOS, movimientos);
        model.addAttribute(ApplicationConstants.ATTR_RESUMEN, resumen);
        model.addAttribute(ApplicationConstants.ATTR_CATEGORIA_ACTUAL, categoria);

        return InfrastructureConstants.VIEW_LISTA_CATEGORIA;
    }

    /**
     * Muestra la página de estadísticas (gráficos)
     */
    @GetMapping(InfrastructureConstants.ENDPOINT_ESTADISTICAS)
    public String mostrarEstadisticas(Model model) {
        model.addAttribute(ApplicationConstants.ATTR_CATEGORIAS, obtenerCategorias());
        return InfrastructureConstants.VIEW_ESTADISTICAS;
    }

    /**
     * Muestra la página de resumen mensual
     */
    @GetMapping(InfrastructureConstants.ENDPOINT_RESUMEN_MENSUAL)
    public String mostrarResumenMensual(Model model) {
        List<com.app.contabilidad.application.dto.ResumenMensualDTO> resumenes = gestionarMovimientosUseCase.obtenerResumenPorMes();
        
        // Calcular totales globales
        java.math.BigDecimal totalGastos = resumenes.stream()
                .map(r -> r.getTotalGastos())
                .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add);
        
        java.math.BigDecimal totalBeneficios = resumenes.stream()
                .map(r -> r.getTotalBeneficios())
                .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add);
        
        java.math.BigDecimal balance = totalBeneficios.subtract(totalGastos);
        
        long totalMovimientos = resumenes.stream()
                .mapToLong(r -> r.getTotalMovimientos())
                .sum();
        
        model.addAttribute(ApplicationConstants.ATTR_RESUMENES_MENSUALES, resumenes);
        model.addAttribute("totalGastosGlobal", totalGastos);
        model.addAttribute("totalBeneficiosGlobal", totalBeneficios);
        model.addAttribute("balanceGlobal", balance);
        model.addAttribute("totalMovimientosGlobal", totalMovimientos);
        
        return InfrastructureConstants.VIEW_RESUMEN_MENSUAL;
    }

    /**
     * Endpoint REST que devuelve totales por categoría para gastos (JSON)
     */
    @GetMapping(InfrastructureConstants.API_ENDPOINT_ESTADISTICAS)
    @ResponseBody
    public java.util.List<com.app.contabilidad.application.dto.CategoriaEstadisticaDTO> apiEstadisticas() {
        var totales = gestionarMovimientosUseCase.obtenerTotalesPorCategoria(com.app.contabilidad.domain.entities.Movimiento.TipoMovimiento.GASTO);

        java.math.BigDecimal totalBeneficios = gestionarMovimientosUseCase.calcularTotalBeneficios();

        java.util.List<com.app.contabilidad.application.dto.CategoriaEstadisticaDTO> lista = new java.util.ArrayList<>();
        totales.forEach((cat, val) -> {
            double porcentaje = 0.0;
            if (totalBeneficios != null && totalBeneficios.compareTo(java.math.BigDecimal.ZERO) > 0) {
                porcentaje = val.divide(totalBeneficios, 4, java.math.RoundingMode.HALF_UP).multiply(new java.math.BigDecimal(100)).doubleValue();
            }
            lista.add(new com.app.contabilidad.application.dto.CategoriaEstadisticaDTO(cat, val, porcentaje));
        });

        // ordenar por total descendente
        lista.sort((a,b) -> b.getTotal().compareTo(a.getTotal()));
        return lista;
    }

    /**
     * Endpoint REST que devuelve el resumen mensual (JSON)
     */
    @GetMapping(InfrastructureConstants.API_ENDPOINT_RESUMEN_MENSUAL)
    @ResponseBody
    public java.util.List<com.app.contabilidad.application.dto.ResumenMensualDTO> apiResumenMensual() {
        return gestionarMovimientosUseCase.obtenerResumenPorMes();
    }

    /**
     * Página de inicio que redirige al listado de movimientos
     */
    @GetMapping(InfrastructureConstants.ENDPOINT_INICIO)
    public String inicio() {
        return InfrastructureConstants.REDIRECT_INICIO;
    }

    /**
     * Calcula el resumen de movimientos
     */
    private ResumenMovimientosDTO calcularResumen(List<Movimiento> movimientos) {
        long gastos = movimientos.stream().filter(m -> m.getTipo() == Movimiento.TipoMovimiento.GASTO).count();
        long beneficios = movimientos.stream().filter(m -> m.getTipo() == Movimiento.TipoMovimiento.BENEFICIO).count();

        return ResumenMovimientosDTO.builder()
                .totalGastos(gestionarMovimientosUseCase.calcularTotalGastos())
                .totalBeneficios(gestionarMovimientosUseCase.calcularTotalBeneficios())
                .balance(gestionarMovimientosUseCase.calcularBalance())
                .cantidadMovimientos((long) movimientos.size())
                .cantidadGastos(gastos)
                .cantidadBeneficios(beneficios)
                .build();
    }

    /**
     * Obtiene las categorías disponibles
     */
    private List<String> obtenerCategorias() {
        return List.of(ApplicationConstants.CATEGORIAS_LISTA);
    }
}
