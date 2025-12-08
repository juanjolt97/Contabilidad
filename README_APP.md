# 💰 Gestor de Gastos y Beneficios del Hogar

Una aplicación web moderna para gestionar ingresos y gastos del hogar, construida con **Spring Boot 4.0.0**, **Java 25**, **H2 Database**, **Thymeleaf** y siguiendo la **arquitectura hexagonal**.

## 🎯 Características

- ✅ **Registrar movimientos**: Crea gastos y beneficios con categorización completa
- ✅ **Dashboard visual**: Resumen de balance, totales de gastos y beneficios en tiempo real
- ✅ **Gestión completa**: Edita, elimina y consulta tus movimientos fácilmente
- ✅ **Categorización avanzada**: Clasifica por Alimentación, Transporte, Servicios, Salud, Educación, Entretenimiento, Hogar, etc.
- ✅ **Diseño responsivo**: Funciona perfectamente en escritorio, tablet y móvil
- ✅ **Base de datos H2**: Sistema de base de datos en memoria (fácil de desplegar)
- ✅ **Arquitectura hexagonal**: Código limpio, testeable y mantenible
- ✅ **Thymeleaf integrado**: Plantillas HTML modernas y dinámicas
- 🎨 **Interfaz responsiva**: Diseño moderno y adaptable a cualquier dispositivo
- 🏗️ **Arquitectura hexagonal**: Separación clara de responsabilidades

## 📋 Requisitos

- Java 25+
- Maven 3.8+

## 🛠️ Instalación y Ejecución

### 1. Clonar el repositorio
```bash
git clone https://github.com/juanjolt97/Contabilidad.git
cd Contabilidad/contabilidad
```

### 2. Compilar el proyecto
```bash
./mvnw clean compile
```

### 3. Ejecutar la aplicación
```bash
./mvnw spring-boot:run
```

La aplicación estará disponible en: `http://localhost:8080`

## 📚 Uso de la Aplicación

### Acceso a la aplicación
- **URL principal**: [http://localhost:8080/movimientos](http://localhost:8080/movimientos)
- **Consola H2**: [http://localhost:8080/h2-console](http://localhost:8080/h2-console)
  - **JDBC URL**: `jdbc:h2:mem:contabilidaddb`
  - **User**: `sa`
  - **Password**: (vacío)

### Funcionalidades principales

#### 📝 Crear un nuevo movimiento
1. Haz clic en "+ Nuevo Movimiento"
2. Completa el formulario con:
   - **Descripción**: Detalles del movimiento
   - **Cantidad**: Monto en euros
   - **Tipo**: Gasto o Beneficio
   - **Fecha**: Día del movimiento
   - **Categoría**: Clasificación del gasto
   - **Notas**: Información adicional (opcional)
3. Haz clic en "Guardar Movimiento"

#### 👁️ Ver movimientos
- **Vista general**: Tabla con todos los movimientos ordenados
- **Filtrar por categoría**: Haz clic en el nombre de la categoría
- **Resumen**: Visualiza totales de gastos, beneficios y balance

#### ✏️ Editar un movimiento
1. En la tabla de movimientos, haz clic en "✏️ Editar"
2. Modifica los datos necesarios
3. Haz clic en "Actualizar Movimiento"

#### 🗑️ Eliminar un movimiento
1. En la tabla de movimientos, haz clic en "🗑️ Eliminar"
2. Confirma la eliminación en el diálogo

## 🏗️ Arquitectura Hexagonal

### Estructura del proyecto

```
src/main/java/com/app/contabilidad/
├── domain/                          # Capa de dominio
│   ├── entities/                   # Entidades de dominio
│   │   └── Movimiento.java        # Entidad principal
│   ├── ports/                      # Puertos (interfaces)
│   │   └── MovimientoRepositoryPort.java  # Contrato de persistencia
│   └── services/                   # Servicios de dominio
│       └── MovimientoService.java  # Lógica de negocio
│
├── application/                     # Capa de aplicación
│   ├── dto/                        # Objetos de transferencia de datos
│   │   ├── CrearMovimientoDTO.java
│   │   ├── ActualizarMovimientoDTO.java
│   │   └── ResumenMovimientosDTO.java
│   └── usecases/                   # Casos de uso
│       └── GestionarMovimientosUseCase.java
│
└── infrastructure/                  # Capa de infraestructura
    ├── adapters/
    │   ├── persistence/            # Adaptador de persistencia
    │   │   ├── MovimientoEntity.java
    │   │   ├── MovimientoJpaRepository.java
    │   │   └── MovimientoRepositoryAdapter.java
    │   └── web/                    # Adaptador web
    │       ├── MovimientosController.java
    │       └── RootController.java
    └── config/                     # Configuración
        └── ApplicationConfig.java
```

### Capas de la arquitectura

#### 🎯 **Capa de Dominio** (`domain/`)
- **Propósito**: Contiene la lógica de negocio pura, independiente de cualquier framework
- **Componentes**:
  - **Entities**: Representan conceptos del dominio (Movimiento)
  - **Ports**: Definen contratos/interfaces que debe cumplir la infraestructura
  - **Services**: Implementan la lógica de negocio

#### 📦 **Capa de Aplicación** (`application/`)
- **Propósito**: Organiza y orquesta los casos de uso
- **Componentes**:
  - **DTOs**: Transfieren datos entre capas (evita exponer entidades)
  - **UseCases**: Orquestan las operaciones del negocio

#### 🔌 **Capa de Infraestructura** (`infrastructure/`)
- **Propósito**: Implementa los detalles técnicos (BD, web, etc.)
- **Componentes**:
  - **Adapters**: Concretan las interfaces (ports) del dominio
  - **Config**: Configuración de Spring y wiring de beans

### Ventajas de esta arquitectura

1. **Independencia del framework**: El dominio no depende de Spring
2. **Testabilidad**: Fácil crear tests unitarios y de integración
3. **Mantenibilidad**: Cada capa tiene responsabilidades claras
4. **Flexibilidad**: Cambiar BD, web framework, etc. sin tocar el dominio
5. **Escalabilidad**: Estructura clara para crecer la aplicación

### Flujo de una petición

```
HTTP Request
    ↓
[Controller] (infrastructure/web)
    ↓
[UseCase] (application)
    ↓
[Service] (domain/services)
    ↓
[Port/Adapter] (domain/ports → infrastructure/adapters)
    ↓
[Database] (H2)
    ↓
HTTP Response
```

## 📊 Base de datos H2

### Esquema
```sql
CREATE TABLE movimientos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    descripcion VARCHAR(255) NOT NULL,
    cantidad DECIMAL(19, 2) NOT NULL,
    tipo ENUM('GASTO', 'BENEFICIO') NOT NULL,
    fecha DATE NOT NULL,
    categoria VARCHAR(100) NOT NULL,
    notas TEXT
);
```

### Datos de prueba
La aplicación incluye algunos movimientos de ejemplo que se cargan automáticamente al iniciar.

## 🧪 Testing (Próximas mejoras)

Se recomienda agregar:
- Tests unitarios para servicios de dominio
- Tests de integración para adaptadores
- Tests de controlador

## 📦 Tecnologías utilizadas

| Tecnología | Versión | Propósito |
|-----------|---------|----------|
| Spring Boot | 4.0.0 | Framework web |
| Spring Data JPA | - | ORM/Persistencia |
| Thymeleaf | - | Motor de plantillas |
| H2 Database | - | Base de datos |
| Lombok | - | Generador de código |
| Jakarta Persistence | - | Especificación JPA |

## 📝 Categorías disponibles

- 🍔 Alimentación
- 🚗 Transporte
- 🔧 Servicios
- ⚕️ Salud
- 📚 Educación
- 🎬 Entretenimiento
- 🏠 Hogar
- 📋 Otros

## 🐛 Troubleshooting

### Puerto 8080 ya está en uso
```bash
# En application.properties, cambia:
server.port=8081
```

### Errores de compilación
```bash
# Limpia y recompila
./mvnw clean install
```

### La BD se vacía al reiniciar
Es el comportamiento esperado con H2 en memoria. Para persistencia:
```properties
# En application.properties:
spring.datasource.url=jdbc:h2:./data/contabilidaddb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
```

## 📄 Licencia

Este proyecto es de uso libre para propósitos educativos y personales.

## 👤 Autor

Juan José López Tejada (juanjolt97)

## 🤝 Contribuciones

Las contribuciones son bienvenidas. Por favor:
1. Fork el repositorio
2. Crea una rama para tu feature (`git checkout -b feature/AmazingFeature`)
3. Commit tus cambios (`git commit -m 'Add some AmazingFeature'`)
4. Push a la rama (`git push origin feature/AmazingFeature`)
5. Abre un Pull Request

---

**¿Necesitas ayuda?** Abre un issue en el repositorio o contacta al autor.
