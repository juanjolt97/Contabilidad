# 📋 Instrucciones de Despliegue y Próximas Mejoras

## 🚀 Despliegue en Producción

### Opción 1: JAR Ejecutable

#### Compilar y empaquetar
```bash
cd contabilidad
./mvnw clean package -DskipTests
```

#### Ejecutar el JAR
```bash
java -jar target/contabilidad-0.0.1-SNAPSHOT.jar
```

#### Con base de datos persistente
```bash
java -Dspring.datasource.url=jdbc:h2:./data/contabilidad \
     -Dspring.jpa.hibernate.ddl-auto=update \
     -jar target/contabilidad-0.0.1-SNAPSHOT.jar
```

### Opción 2: Docker

#### Crear Dockerfile
```dockerfile
FROM openjdk:25-slim

WORKDIR /app

COPY target/contabilidad-0.0.1-SNAPSHOT.jar app.jar

ENV SPRING_DATASOURCE_URL=jdbc:h2:mem:contabilidaddb
ENV SPRING_JPA_HIBERNATE_DDL_AUTO=create-drop

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
```

#### Construir imagen
```bash
docker build -t contabilidad:latest .
```

#### Ejecutar contenedor
```bash
docker run -p 8080:8080 \
  -e SPRING_DATASOURCE_URL=jdbc:h2:./data/contabilidad \
  contabilidad:latest
```

### Opción 3: Base de Datos PostgreSQL (Recomendado para Producción)

#### 1. Instalar PostgreSQL
```bash
# Windows
choco install postgresql

# macOS
brew install postgresql

# Linux
sudo apt-get install postgresql postgresql-contrib
```

#### 2. Crear base de datos
```sql
CREATE DATABASE contabilidad;
CREATE USER contabilidad_user WITH PASSWORD 'secure_password';
GRANT ALL PRIVILEGES ON DATABASE contabilidad TO contabilidad_user;
```

#### 3. Actualizar `application.properties`
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/contabilidad
spring.datasource.username=contabilidad_user
spring.datasource.password=secure_password
spring.datasource.driver-class-name=org.postgresql.Driver
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.hibernate.ddl-auto=update
```

#### 4. Agregar dependencia en `pom.xml`
```xml
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
    <version>42.7.1</version>
</dependency>
```

## 📊 Monitoreo y Métricas

### Habilitar Actuator (recomendado)

#### Agregar dependencia en `pom.xml`
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

#### Endpoints disponibles
- `GET /actuator/health` - Estado de la aplicación
- `GET /actuator/metrics` - Métricas disponibles
- `GET /actuator/env` - Variables de entorno

### Logging

#### Configurar en `application.properties`
```properties
# Nivel de log
logging.level.root=INFO
logging.level.com.app.contabilidad=DEBUG

# Archivo de log
logging.file.name=logs/contabilidad.log
logging.file.max-size=10MB
logging.file.max-history=10
```

## 🔐 Seguridad

### Mejoras de Seguridad Recomendadas

#### 1. Agregar Spring Security
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>
```

#### 2. Validación y Sanitización
```java
@Data
public class CrearMovimientoDTO {
    @NotBlank(message = "La descripción es requerida")
    @Size(min = 3, max = 255)
    private String descripcion;

    @NotNull(message = "La cantidad es requerida")
    @DecimalMin("0.01")
    private BigDecimal cantidad;
    
    // ...
}
```

#### 3. Encriptación de Datos Sensibles
```properties
spring.datasource.password={cipher}encrypted_password
spring.datasource.username={cipher}encrypted_username
```

## 🧪 Testing

### Tests Unitarios

#### Test del Servicio
```java
@ExtendWith(MockitoExtension.class)
class MovimientoServiceTest {
    
    @Mock
    private MovimientoRepositoryPort repository;
    
    @InjectMocks
    private MovimientoService service;
    
    @Test
    void testCrearMovimientoValido() {
        // Arrange
        Movimiento movimiento = Movimiento.builder()
                .descripcion("Test")
                .cantidad(BigDecimal.valueOf(100))
                .tipo(Movimiento.TipoMovimiento.GASTO)
                .fecha(LocalDate.now())
                .categoria("Otros")
                .build();
        
        when(repository.guardar(any())).thenReturn(movimiento);
        
        // Act
        Movimiento resultado = service.crearMovimiento(movimiento);
        
        // Assert
        assertNotNull(resultado);
        assertEquals("Test", resultado.getDescripcion());
        verify(repository).guardar(any());
    }
    
    @Test
    void testCrearMovimientoInvalido() {
        // Arrange
        Movimiento movimiento = Movimiento.builder()
                .descripcion("") // Inválido
                .cantidad(BigDecimal.ZERO) // Inválido
                .tipo(Movimiento.TipoMovimiento.GASTO)
                .fecha(LocalDate.now())
                .categoria("Otros")
                .build();
        
        // Act & Assert
        assertThrows(IllegalArgumentException.class, 
            () -> service.crearMovimiento(movimiento));
    }
}
```

### Tests de Integración

```java
@SpringBootTest
@AutoConfigureMockMvc
class MovimientosControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private MovimientoJpaRepository repository;
    
    @Test
    void testCrearMovimientoViaWeb() throws Exception {
        mockMvc.perform(post("/movimientos")
                .param("descripcion", "Test")
                .param("cantidad", "50.00")
                .param("tipo", "GASTO")
                .param("fecha", "2024-12-08")
                .param("categoria", "Otros"))
                .andExpect(status().is3xxRedirection());
        
        assertEquals(1, repository.count());
    }
}
```

### Ejecutar Tests
```bash
./mvnw test
./mvnw test -Dtest=MovimientoServiceTest
./mvnw verify
```

## 📈 Próximas Mejoras

### Fase 1: Funcionalidades Core (Prioritario)

- [ ] **Búsqueda avanzada**
  - [ ] Filtrar por rango de fechas
  - [ ] Filtrar por rango de cantidad
  - [ ] Búsqueda por texto

- [ ] **Reportes**
  - [ ] Reporte mensual
  - [ ] Reporte por categoría
  - [ ] Gráficas de tendencias

- [ ] **Autenticación y Autorización**
  - [ ] Login de usuario
  - [ ] Cada usuario vea sus propios movimientos
  - [ ] Roles (admin, usuario normal)

### Fase 2: Mejoras de UX/UI

- [ ] **Interfaz mejorada**
  - [ ] Dashboard con gráficas (Chart.js)
  - [ ] Modo oscuro
  - [ ] Responsive design mejorado

- [ ] **Validación en cliente**
  - [ ] Validación JavaScript
  - [ ] Autocompletar categorías
  - [ ] Calendario interactivo

- [ ] **Exportación de datos**
  - [ ] Exportar a CSV
  - [ ] Exportar a PDF
  - [ ] Exportar a Excel

### Fase 3: Escalabilidad y Performance

- [ ] **Caché**
  - [ ] Redis para datos frecuentes
  - [ ] Cache de categorías

- [ ] **API REST**
  - [ ] Endpoints JSON
  - [ ] Documentación OpenAPI/Swagger

- [ ] **Base de datos**
  - [ ] Índices optimizados
  - [ ] Particionamiento para años

- [ ] **Microservicios** (si crece mucho)
  - [ ] Servicio de movimientos
  - [ ] Servicio de reportes
  - [ ] Servicio de notificaciones

### Fase 4: Funcionalidades Avanzadas

- [ ] **Notificaciones**
  - [ ] Alertas de gastos altos
  - [ ] Recordatorio de presupuestos

- [ ] **Presupuestos**
  - [ ] Establecer límites por categoría
  - [ ] Alertas cuando se supera

- [ ] **Etiquetas**
  - [ ] Etiquetar movimientos
  - [ ] Filtrar por etiquetas

- [ ] **Importación de datos**
  - [ ] Cargar CSV
  - [ ] Conectar con bancos

- [ ] **Moneda múltiple**
  - [ ] Soporte para EUR, USD, etc.
  - [ ] Conversión automática

## 🛠️ Mejora Técnica del Código

### Código Actual
```java
// ❌ Constantes duplicadas
redirectAttributes.addFlashAttribute("mensaje", "Creado");
redirectAttributes.addFlashAttribute("error", "Error");
return "redirect:/movimientos";
```

### Mejora Recomendada
```java
// ✅ Usar constantes
public class RedirectMessages {
    public static final String EXITO = "mensaje";
    public static final String ERROR = "error";
    public static final String REDIRECT = "redirect:/movimientos";
}

// Uso:
redirectAttributes.addFlashAttribute(RedirectMessages.EXITO, "Creado");
redirectAttributes.addFlashAttribute(RedirectMessages.ERROR, "Error");
return RedirectMessages.REDIRECT;
```

### Validación Mejorada
```java
// ❌ Validación manual
if (!dto.getDescripcion().isEmpty()) { }

// ✅ Usar anotaciones de validación
@Data
public class CrearMovimientoDTO {
    @NotBlank
    @Size(min = 3, max = 255)
    private String descripcion;
    
    @NotNull
    @DecimalMin("0.01")
    private BigDecimal cantidad;
}

// En controlador:
public String crear(@Valid @ModelAttribute CrearMovimientoDTO dto,
                   BindingResult errors) {
    if (errors.hasErrors()) {
        // Manejar errores
    }
}
```

## 📚 Recursos Adicionales

### Documentación
- [Spring Boot Official Docs](https://spring.io/projects/spring-boot)
- [Spring Data JPA](https://spring.io/projects/spring-data-jpa)
- [Thymeleaf Documentation](https://www.thymeleaf.org/)
- [Arquitectura Hexagonal](https://alistair.cockburn.us/hexagonal-architecture/)

### Herramientas Recomendadas
- **SonarQube**: Análisis de calidad de código
- **JUnit 5**: Testing unitario
- **Postman**: Testing de APIs
- **Docker**: Containerización
- **GitHub Actions**: CI/CD

## 📞 Soporte y Contacto

En caso de dudas o problemas:
1. Revisa la documentación en `README_APP.md`
2. Consulta `ARQUITECTURA_HEXAGONAL.md` para entender el código
3. Abre un issue en GitHub
4. Contacta al autor

---

**Última actualización**: 8 de diciembre de 2024
**Versión**: 0.0.1-SNAPSHOT
**Estado**: En desarrollo
