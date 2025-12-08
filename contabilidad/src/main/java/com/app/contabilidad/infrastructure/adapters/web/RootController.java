package com.app.contabilidad.infrastructure.adapters.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controlador para manejar la ruta raíz
 */
@Controller
public class RootController {
    
    @GetMapping("/")
    public String root() {
        return "redirect:/movimientos";
    }
}
