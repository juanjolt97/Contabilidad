package com.app.contabilidad.infrastructure.config;

import com.app.contabilidad.domain.ports.MovimientoRepositoryPort;
import com.app.contabilidad.domain.services.MovimientoService;
import com.app.contabilidad.application.usecases.GestionarMovimientosUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración de los beans de la aplicación siguiendo arquitectura hexagonal
 */
@Configuration
public class ApplicationConfig {

    @Bean
    public MovimientoService movimientoService(MovimientoRepositoryPort repository) {
        return new MovimientoService(repository);
    }

    @Bean
    public GestionarMovimientosUseCase gestionarMovimientosUseCase(MovimientoService movimientoService) {
        return new GestionarMovimientosUseCase(movimientoService);
    }
}
