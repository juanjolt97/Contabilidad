package com.app.contabilidad.infrastructure.adapters.web;

import com.app.contabilidad.application.usecases.GestionarMovimientosUseCase;
import com.app.contabilidad.application.dto.CrearMovimientoDTO;
import com.app.contabilidad.application.dto.ResumenMovimientosDTO;
import com.app.contabilidad.domain.entities.Movimiento;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

/**
 * Controlador web que adapta las peticiones HTTP a los casos de uso
 * Adaptador web de la arquitectura hexagonal
 */
@Controller
@RequestMapping("/movimientos")
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

        model.addAttribute("movimientos", movimientos);
        model.addAttribute("resumen", resumen);
        model.addAttribute("nuevo", new CrearMovimientoDTO());

        return "movimientos/lista";
    }

    /**
     * Muestra el formulario para crear un nuevo movimiento
     */
    @GetMapping("/nuevo")
    public String mostrarFormularioCrear(Model model) {
        model.addAttribute("movimiento", new CrearMovimientoDTO());
        model.addAttribute("tiposMovimiento", Movimiento.TipoMovimiento.values());
        model.addAttribute("categorias", obtenerCategorias());
        return "movimientos/formulario";
    }

    /**
     * Crea un nuevo movimiento
     */
    @PostMapping
    public String crearMovimiento(@ModelAttribute CrearMovimientoDTO dto, RedirectAttributes redirectAttributes) {
        try {
            gestionarMovimientosUseCase.crearMovimiento(dto);
            redirectAttributes.addFlashAttribute("mensaje", "Movimiento creado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al crear el movimiento: " + e.getMessage());
        }
        return "redirect:/movimientos";
    }

    /**
     * Muestra el formulario para editar un movimiento
     */
    @GetMapping("/{id}/editar")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
        var movimiento = gestionarMovimientosUseCase.obtenerMovimiento(id);

        if (movimiento.isEmpty()) {
            return "redirect:/movimientos";
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

        model.addAttribute("id", id);
        model.addAttribute("movimiento", dto);
        model.addAttribute("tiposMovimiento", Movimiento.TipoMovimiento.values());
        model.addAttribute("categorias", obtenerCategorias());
        return "movimientos/formulario-editar";
    }

    /**
     * Actualiza un movimiento
     */
    @PostMapping("/{id}")
    public String actualizarMovimiento(@PathVariable Long id, @ModelAttribute CrearMovimientoDTO dto, RedirectAttributes redirectAttributes) {
        try {
            gestionarMovimientosUseCase.actualizarMovimiento(id, dto);
            redirectAttributes.addFlashAttribute("mensaje", "Movimiento actualizado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al actualizar el movimiento: " + e.getMessage());
        }
        return "redirect:/movimientos";
    }

    /**
     * Elimina un movimiento
     */
    @GetMapping("/{id}/eliminar")
    public String eliminarMovimiento(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            gestionarMovimientosUseCase.eliminarMovimiento(id);
            redirectAttributes.addFlashAttribute("mensaje", "Movimiento eliminado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar el movimiento: " + e.getMessage());
        }
        return "redirect:/movimientos";
    }

    /**
     * Muestra los movimientos filtrados por categoría
     */
    @GetMapping("/categoria/{categoria}")
    public String listarPorCategoria(@PathVariable String categoria, Model model) {
        List<Movimiento> movimientos = gestionarMovimientosUseCase.obtenerMovimientosPorCategoria(categoria);
        ResumenMovimientosDTO resumen = calcularResumen(movimientos);

        model.addAttribute("movimientos", movimientos);
        model.addAttribute("resumen", resumen);
        model.addAttribute("categoriaActual", categoria);

        return "movimientos/lista-categoria";
    }

    /**
     * Página de inicio que redirige al listado de movimientos
     */
    @GetMapping("/inicio")
    public String inicio() {
        return "redirect:/movimientos";
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
        return List.of(
                "Alimentación",
                "Transporte",
                "Servicios",
                "Salud",
                "Educación",
                "Entretenimiento",
                "Hogar",
                "Otros"
        );
    }
}
