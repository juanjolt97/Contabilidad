# 🏗️ Guía de Arquitectura Hexagonal - Contabilidad del Hogar

## Índice
1. [Introducción](#introducción)
2. [Conceptos Fundamentales](#conceptos-fundamentales)
3. [Estructura del Proyecto](#estructura-del-proyecto)
4. [Capas de la Arquitectura](#capas-de-la-arquitectura)
5. [Patrones y Principios](#patrones-y-principios)
6. [Ejemplos Prácticos](#ejemplos-prácticos)
7. [Cómo Extender la Aplicación](#cómo-extender-la-aplicación)

## Introducción

La **arquitectura hexagonal** (también conocida como arquitectura de puertos y adaptadores) es un patrón arquitectónico que promueve la separación de responsabilidades y la independencia de los detalles de implementación.

### ¿Por qué arquitectura hexagonal?

```
Ventajas:
✅ El dominio es independiente de frameworks
✅ Fácil de testear
✅ Flexible para cambios tecnológicos
✅ Código más mantenible y limpio
✅ Escalable
```

## Conceptos Fundamentales

### 1. **Puertos** (Ports)
Un puerto es una **interfaz** que define un contrato que otros módulos deben cumplir.

```java
// Port: Contrato para acceder a movimientos
public interface MovimientoRepositoryPort {
    Movimiento guardar(Movimiento movimiento);
    Optional<Movimiento> obtenerPorId(Long id);
    List<Movimiento> obtenerTodos();
    // ... más métodos
}
```

**Tipos de puertos:**
- **Puertos de entrada**: Definen cómo entra información a la aplicación (APIs, Controladores)
- **Puertos de salida**: Definen cómo sale información (Base de datos, APIs externas)

### 2. **Adaptadores** (Adapters)
Un adaptador es una **implementación concreta** de un puerto.

```java
// Adapter: Implementa el puerto para H2/JPA
@Component
@RequiredArgsConstructor
public class MovimientoRepositoryAdapter implements MovimientoRepositoryPort {
    private final MovimientoJpaRepository jpaRepository;
    
    @Override
    public Movimiento guardar(Movimiento movimiento) {
        // Implementación específica para JPA
    }
}
```

**Tipos de adaptadores:**
- **Adaptadores primarios**: Reciben peticiones (Controllers)
- **Adaptadores secundarios**: Realizan tareas técnicas (Repositories, Email, etc.)

### 3. **Dominio** (Domain)
El corazón de la aplicación, completamente independiente de la tecnología.

```java
// Entidad de dominio pura
public class Movimiento {
    private Long id;
    private String descripcion;
    private BigDecimal cantidad;
    private TipoMovimiento tipo;
    // ... métodos de negocio
    
    public boolean esValido() {
        return descripcion != null && cantidad.compareTo(BigDecimal.ZERO) > 0;
    }
}
```

## Estructura del Proyecto

```
contabilidad/
├── src/main/java/com/app/contabilidad/
│   ├── ContabilidadApplication.java          ← Punto de entrada
│   │
│   ├── domain/                               ← 🎯 CAPA DE DOMINIO
│   │   ├── entities/
│   │   │   └── Movimiento.java             ← Entidad pura
│   │   ├── ports/
│   │   │   └── MovimientoRepositoryPort.java ← Puerto (interfaz)
│   │   └── services/
│   │       └── MovimientoService.java       ← Lógica de negocio
│   │
│   ├── application/                          ← 📦 CAPA DE APLICACIÓN
│   │   ├── dto/
│   │   │   ├── CrearMovimientoDTO.java
│   │   │   ├── ActualizarMovimientoDTO.java
│   │   │   └── ResumenMovimientosDTO.java
│   │   └── usecases/
│   │       └── GestionarMovimientosUseCase.java ← Casos de uso
│   │
│   └── infrastructure/                       ← 🔌 CAPA DE INFRAESTRUCTURA
│       ├── adapters/
│       │   ├── persistence/
│       │   │   ├── MovimientoEntity.java           ← Mapeo JPA
│       │   │   ├── MovimientoJpaRepository.java    ← Spring Data
│       │   │   └── MovimientoRepositoryAdapter.java ← Implementación del puerto
│       │   └── web/
│       │       ├── MovimientosController.java  ← Adaptador primario
│       │       └── RootController.java         ← Adaptador primario
│       └── config/
│           └── ApplicationConfig.java  ← Configuración de beans
│
└── src/main/resources/
    ├── application.properties
    ├── data.sql                    ← Datos iniciales
    ├── templates/
    │   └── movimientos/            ← Vistas Thymeleaf
    │       ├── lista.html
    │       ├── formulario.html
    │       ├── formulario-editar.html
    │       └── lista-categoria.html
    └── static/
        └── css/
            └── style.css
```

## Capas de la Arquitectura

### 🎯 Capa de Dominio (Domain)

La capa más importante. **No depende de nada externo**.

#### Características:
- ✅ Sin dependencias de Spring
- ✅ Sin JPA, sin Thymeleaf, sin bases de datos
- ✅ Contiene las reglas de negocio puras
- ✅ 100% testeable sin mockear frameworks

#### Componentes:

**Entidades:**
```java
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Movimiento {
    private Long id;
    private String descripcion;
    private BigDecimal cantidad;
    private TipoMovimiento tipo;
    private LocalDate fecha;
    private String categoria;
    private String notas;

    // Regla de negocio: validación
    public boolean esValido() {
        return descripcion != null && !descripcion.trim().isEmpty()
                && cantidad != null && cantidad.compareTo(BigDecimal.ZERO) > 0
                && tipo != null && fecha != null;
    }
}
```

**Puertos (Interfaces):**
```java
public interface MovimientoRepositoryPort {
    Movimiento guardar(Movimiento movimiento);
    Optional<Movimiento> obtenerPorId(Long id);
    List<Movimiento> obtenerTodos();
    Movimiento actualizar(Movimiento movimiento);
    void eliminar(Long id);
    List<Movimiento> obtenerPorTipo(Movimiento.TipoMovimiento tipo);
    List<Movimiento> obtenerPorCategoria(String categoria);
}
```

**Servicios de Dominio:**
```java
public class MovimientoService {
    private final MovimientoRepositoryPort repository;

    public MovimientoService(MovimientoRepositoryPort repository) {
        this.repository = repository;
    }

    // Lógica de negocio: crear movimiento
    public Movimiento crearMovimiento(Movimiento movimiento) {
        if (!movimiento.esValido()) {
            throw new IllegalArgumentException("El movimiento no es válido");
        }
        return repository.guardar(movimiento);
    }

    // Lógica de negocio: calcular balance
    public BigDecimal calcularBalance() {
        return calcularTotalBeneficios().subtract(calcularTotalGastos());
    }
}
```

### 📦 Capa de Aplicación (Application)

Orquesta la interacción entre el dominio y la infraestructura.

#### Características:
- ✅ Coordina casos de uso
- ✅ Transforma datos entre capas (DTOs)
- ✅ Implementa lógica de flujo de aplicación
- ✅ Depende del dominio, pero no de la infraestructura

#### Componentes:

**DTOs (Objetos de Transferencia):**
```java
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CrearMovimientoDTO {
    private String descripcion;
    private BigDecimal cantidad;
    private String tipo; // "GASTO" o "BENEFICIO"
    private LocalDate fecha;
    private String categoria;
    private String notas;
}
```

**Casos de Uso:**
```java
public class GestionarMovimientosUseCase {
    private final MovimientoService service;

    public GestionarMovimientosUseCase(MovimientoService service) {
        this.service = service;
    }

    // Caso de uso: crear movimiento
    public Movimiento crearMovimiento(CrearMovimientoDTO dto) {
        Movimiento movimiento = Movimiento.builder()
                .descripcion(dto.getDescripcion())
                .cantidad(dto.getCantidad())
                .tipo(Movimiento.TipoMovimiento.valueOf(dto.getTipo()))
                .fecha(dto.getFecha() != null ? dto.getFecha() : LocalDate.now())
                .categoria(dto.getCategoria())
                .notas(dto.getNotas())
                .build();

        return service.crearMovimiento(movimiento);
    }
}
```

### 🔌 Capa de Infraestructura (Infrastructure)

Detalles técnicos: BD, web, configuración.

#### Características:
- ✅ Implementa los puertos del dominio
- ✅ Maneja detalles técnicos (JPA, Spring, etc.)
- ✅ Puede cambiar sin afectar el dominio
- ✅ Depende de todas las capas

#### Adaptadores Secundarios (Persistencia):

```java
@Component
@RequiredArgsConstructor
public class MovimientoRepositoryAdapter implements MovimientoRepositoryPort {
    private final MovimientoJpaRepository jpaRepository;

    @Override
    public Movimiento guardar(Movimiento movimiento) {
        // Convierte entidad de dominio a entidad JPA
        MovimientoEntity entity = toEntity(movimiento);
        // Guarda en BD usando Spring Data
        MovimientoEntity saved = jpaRepository.save(entity);
        // Convierte de vuelta a dominio
        return toDomain(saved);
    }

    // Conversión de Dominio a JPA
    private MovimientoEntity toEntity(Movimiento domainEntity) {
        return MovimientoEntity.builder()
                .id(domainEntity.getId())
                .descripcion(domainEntity.getDescripcion())
                .cantidad(domainEntity.getCantidad())
                .tipo(MovimientoEntity.TipoMovimiento.valueOf(
                    domainEntity.getTipo().name()))
                .fecha(domainEntity.getFecha())
                .categoria(domainEntity.getCategoria())
                .notas(domainEntity.getNotas())
                .build();
    }

    // Conversión de JPA a Dominio
    private Movimiento toDomain(MovimientoEntity entity) {
        return Movimiento.builder()
                .id(entity.getId())
                .descripcion(entity.getDescripcion())
                .cantidad(entity.getCantidad())
                .tipo(Movimiento.TipoMovimiento.valueOf(entity.getTipo().name()))
                .fecha(entity.getFecha())
                .categoria(entity.getCategoria())
                .notas(entity.getNotas())
                .build();
    }
}
```

#### Adaptadores Primarios (Web/Controllers):

```java
@Controller
@RequestMapping("/movimientos")
@RequiredArgsConstructor
public class MovimientosController {
    private final GestionarMovimientosUseCase gestionarMovimientos;

    // Adaptador primario: recibe HTTP request
    @PostMapping
    public String crearMovimiento(@ModelAttribute CrearMovimientoDTO dto, 
                                 RedirectAttributes redirectAttributes) {
        try {
            // Delega al caso de uso
            gestionarMovimientos.crearMovimiento(dto);
            redirectAttributes.addFlashAttribute("mensaje", 
                "Movimiento creado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/movimientos";
    }
}
```

#### Configuración de Beans:

```java
@Configuration
public class ApplicationConfig {
    // Inyección de dependencias siguiendo arquitectura hexagonal
    
    @Bean
    public MovimientoService movimientoService(
            MovimientoRepositoryPort repository) {
        return new MovimientoService(repository);
    }

    @Bean
    public GestionarMovimientosUseCase gestionarMovimientosUseCase(
            MovimientoService service) {
        return new GestionarMovimientosUseCase(service);
    }
}
```

## Patrones y Principios

### SOLID

#### Single Responsibility Principle (SRP)
- `MovimientoService`: Solo lógica de negocio
- `MovimientoRepositoryAdapter`: Solo persistencia
- `MovimientosController`: Solo manejo HTTP

#### Open/Closed Principle (OCP)
```java
// Abierto para extensión
public interface MovimientoRepositoryPort {
    // Define el contrato
}

// Cerrado para modificación
@Component
public class MovimientoRepositoryAdapter implements MovimientoRepositoryPort {
    // Una implementación
}

// Puedes agregar más adaptadores sin cambiar el puerto
@Component
public class MovimientoMongoAdapter implements MovimientoRepositoryPort {
    // Otra implementación para MongoDB
}
```

#### Liskov Substitution Principle (LSP)
```java
// Cualquier adaptador que implemente el puerto puede usarse
MovimientoRepositoryPort repository = new MovimientoRepositoryAdapter(...);
// O con otra BD:
MovimientoRepositoryPort repository = new MovimientoMongoAdapter(...);
```

#### Interface Segregation Principle (ISP)
El puerto `MovimientoRepositoryPort` define exactamente lo que necesita el dominio.

#### Dependency Inversion Principle (DIP)
```java
// ❌ Mala: El servicio depende de una implementación concreta
public class MovimientoService {
    private MovimientoJpaRepository repository; // Concreta
}

// ✅ Buena: El servicio depende de la interfaz (puerto)
public class MovimientoService {
    private MovimientoRepositoryPort repository; // Abstracción
}
```

### Otros Patrones

#### DTO (Data Transfer Object)
```java
// Transporta datos sin exponer la entidad de dominio
public class CrearMovimientoDTO {
    private String descripcion;
    private BigDecimal cantidad;
    // ...
}
```

#### Adapter Pattern
```java
// Adapta una interfaz a otra
public class MovimientoRepositoryAdapter implements MovimientoRepositoryPort {
    private final MovimientoJpaRepository jpaRepository;
    // Adapta JPA al puerto
}
```

#### Builder Pattern
```java
// Construcción fluida y segura
Movimiento movimiento = Movimiento.builder()
        .descripcion("Compra")
        .cantidad(BigDecimal.valueOf(50))
        .tipo(TipoMovimiento.GASTO)
        .fecha(LocalDate.now())
        .categoria("Alimentación")
        .build();
```

## Ejemplos Prácticos

### Caso de Uso: Crear un Movimiento

#### Paso 1: Usuario envía petición HTTP
```html
<form th:action="@{/movimientos}" method="post">
    <input name="descripcion" value="Compra alimentos">
    <input name="cantidad" value="50.00">
    <select name="tipo">
        <option value="GASTO">Gasto</option>
    </select>
</form>
```

#### Paso 2: Controlador recibe la petición
```java
@PostMapping
public String crearMovimiento(@ModelAttribute CrearMovimientoDTO dto, ...) {
    // El DTO contiene los datos del formulario
    gestionarMovimientos.crearMovimiento(dto);
    return "redirect:/movimientos";
}
```

#### Paso 3: Caso de uso orquesta
```java
public Movimiento crearMovimiento(CrearMovimientoDTO dto) {
    // Crea entidad de dominio desde DTO
    Movimiento movimiento = Movimiento.builder()
            .descripcion(dto.getDescripcion())
            .cantidad(dto.getCantidad())
            .tipo(Movimiento.TipoMovimiento.valueOf(dto.getTipo()))
            .fecha(LocalDate.now())
            .categoria(dto.getCategoria())
            .build();

    // Delega al servicio de dominio
    return movimientoService.crearMovimiento(movimiento);
}
```

#### Paso 4: Servicio de dominio valida y guarda
```java
public Movimiento crearMovimiento(Movimiento movimiento) {
    // Valida según reglas de negocio
    if (!movimiento.esValido()) {
        throw new IllegalArgumentException("Movimiento inválido");
    }

    // Persiste usando el puerto (sin saber cómo)
    return movimientoRepository.guardar(movimiento);
}
```

#### Paso 5: Adaptador implementa la persistencia
```java
@Override
public Movimiento guardar(Movimiento movimiento) {
    // Convierte a entidad JPA
    MovimientoEntity entity = toEntity(movimiento);

    // Guarda en BD
    MovimientoEntity saved = jpaRepository.save(entity);

    // Convierte de vuelta a dominio
    return toDomain(saved);
}
```

#### Paso 6: Base de datos almacena
```sql
INSERT INTO movimientos (descripcion, cantidad, tipo, fecha, categoria)
VALUES ('Compra alimentos', 50.00, 'GASTO', '2024-12-08', 'Alimentación');
```

#### Paso 7: Respuesta al usuario
```
✅ Movimiento creado exitosamente
```

### Flujo Completo de Datos

```
HTTP Request (POST /movimientos)
    ↓
[Controller] Recibe CrearMovimientoDTO
    ↓
[UseCase] Orquesta la creación
    ↓
[Service] Valida según reglas de negocio
    ↓
[Port] Define cómo persistir (interfaz)
    ↓
[Adapter] Implementa la persistencia (MovimientoRepositoryAdapter)
    ↓
[JPA] Mapea a entidad de BD
    ↓
[H2 Database] Almacena físicamente
    ↓
[Adapter] Convierte respuesta
    ↓
[Service] Retorna entidad de dominio
    ↓
[UseCase] Orquesta respuesta
    ↓
[Controller] Redirige y muestra mensaje
    ↓
HTTP Response (Redirect + Flash Message)
```

## Cómo Extender la Aplicación

### Agregar una Nueva Característica

#### Ejemplo: Exportar movimientos a CSV

##### 1. Definir el Puerto (Domain)
```java
// domain/ports/ExportadorMovimientosPort.java
public interface ExportadorMovimientosPort {
    byte[] exportarACSV(List<Movimiento> movimientos);
}
```

##### 2. Crear el Caso de Uso (Application)
```java
// application/usecases/ExportarMovimientosUseCase.java
public class ExportarMovimientosUseCase {
    private final ExportadorMovimientosPort exportador;

    public byte[] exportarCSV() {
        List<Movimiento> todos = 
            gestionarMovimientos.listarMovimientos();
        return exportador.exportarACSV(todos);
    }
}
```

##### 3. Implementar el Adaptador (Infrastructure)
```java
// infrastructure/adapters/export/CSVExportAdapter.java
@Component
public class CSVExportAdapter implements ExportadorMovimientosPort {
    @Override
    public byte[] exportarACSV(List<Movimiento> movimientos) {
        // Implementación de CSV
        StringBuilder csv = new StringBuilder();
        csv.append("Descripción,Cantidad,Tipo,Fecha,Categoría\n");
        for (Movimiento m : movimientos) {
            csv.append(String.format("%s,%s,%s,%s,%s\n",
                m.getDescripcion(),
                m.getCantidad(),
                m.getTipo(),
                m.getFecha(),
                m.getCategoria()
            ));
        }
        return csv.toString().getBytes(StandardCharsets.UTF_8);
    }
}
```

##### 4. Agregar al Controlador (Infrastructure)
```java
// infrastructure/adapters/web/ExportController.java
@Controller
@RequestMapping("/export")
@RequiredArgsConstructor
public class ExportController {
    private final ExportarMovimientosUseCase exportarUseCase;

    @GetMapping("/csv")
    public ResponseEntity<byte[]> exportarCSV() {
        byte[] data = exportarUseCase.exportarCSV();
        return ResponseEntity.ok()
            .header("Content-Disposition", 
                "attachment; filename=movimientos.csv")
            .body(data);
    }
}
```

##### 5. Registrar el Bean (Infrastructure)
```java
// infrastructure/config/ApplicationConfig.java
@Bean
public ExportarMovimientosUseCase exportarMovimientosUseCase(
        ExportadorMovimientosPort exportador) {
    return new ExportarMovimientosUseCase(exportador);
}
```

### Cambiar la Base de Datos

#### De H2 a PostgreSQL

##### 1. Agregar dependencia en `pom.xml`
```xml
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
    <version>42.7.1</version>
</dependency>
```

##### 2. Cambiar `application.properties`
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/contabilidad
spring.datasource.username=postgres
spring.datasource.password=password
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
```

##### 3. ¡Listo! 
No necesitas cambiar nada más. La arquitectura hexagonal permite este cambio porque:
- El dominio no sabe que existe JPA
- El adaptador implementa el puerto
- El puerto no cambió
- Spring maneja el cambio automáticamente

### Agregar Pruebas

#### Test Unitario del Servicio
```java
@ExtendWith(MockitoExtension.class)
class MovimientoServiceTest {
    
    @Mock
    MovimientoRepositoryPort repository;

    @InjectMocks
    MovimientoService service;

    @Test
    void crearMovimientoValido() {
        // Arrange
        Movimiento movimiento = Movimiento.builder()
                .descripcion("Compra")
                .cantidad(BigDecimal.valueOf(50))
                .tipo(TipoMovimiento.GASTO)
                .fecha(LocalDate.now())
                .categoria("Alimentación")
                .build();

        when(repository.guardar(any())).thenReturn(movimiento);

        // Act
        Movimiento resultado = service.crearMovimiento(movimiento);

        // Assert
        assertNotNull(resultado);
        assertEquals("Compra", resultado.getDescripcion());
        verify(repository).guardar(any());
    }
}
```

## Conclusión

La arquitectura hexagonal proporciona una **base sólida y flexible** para desarrollar aplicaciones mantenibles y escalables. 

**Principios clave:**
1. El dominio es independiente
2. Los puertos definen los contratos
3. Los adaptadores implementan los detalles
4. El flujo es siempre hacia adentro (hacia el dominio)

¡Ahora estás listo para extender y mantener esta aplicación! 🚀
