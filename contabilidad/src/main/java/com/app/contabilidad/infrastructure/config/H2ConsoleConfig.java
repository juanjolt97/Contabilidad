package com.app.contabilidad.infrastructure.config;

import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Registra el servlet de la consola H2 en /h2-console/* usando la clase
 * compatible con Jakarta Servlet API si está disponible en el classpath.
 */
@Configuration
public class H2ConsoleConfig {

    @Bean
    public ServletContextInitializer h2ConsoleServletInitializer() {
        return servletContext -> {
            // Usar la clase JakartaWebServlet si existe en la versión de H2
            servletContext.addServlet("H2Console", "org.h2.server.web.JakartaWebServlet")
                    .addMapping("/h2-console/*");
        };
    }
}
