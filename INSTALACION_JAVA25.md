# 🚀 Instrucciones para Instalar Java 25 JDK

## ❌ Problema Actual

Actualmente tienes instalado **JRE (Java Runtime Environment)** pero necesitas **JDK (Java Development Kit)** para compilar el proyecto.

```
Error: No compiler is provided in this environment. 
Perhaps you are running on a JRE rather than a JDK?
```

## ✅ Solución: Descargar e Instalar Java 25 JDK

### Opción 1: Descargar desde Oracle (Recomendado)

#### Paso 1: Descargar Java 25 JDK
1. Ve a: https://www.oracle.com/java/technologies/downloads/#java25
2. Descarga la versión **Windows x64 Installer** (para tu sistema operativo)
3. El archivo tendrá un nombre como: `jdk-25_windows-x64_bin.exe`

#### Paso 2: Instalar Java 25
1. Ejecuta el instalador descargado
2. Haz clic en "Siguiente >" para continuar
3. Acepta la licencia de Oracle
4. Elige la ruta de instalación (por defecto: `C:\Program Files\Java\jdk-25`)
5. Completa la instalación

#### Paso 3: Configurar Variables de Entorno

**En Windows (PowerShell como Administrador):**

```powershell
# 1. Abre PowerShell como Administrador

# 2. Ejecuta estos comandos:
[Environment]::SetEnvironmentVariable('JAVA_HOME', 'C:\Program Files\Java\jdk-25', 'User')

# 3. Cierra y reabre PowerShell para que los cambios tengan efecto

# 4. Verifica que Java 25 JDK está instalado:
java -version
javac -version
```

**Salida esperada:**
```
java version "25" 2024-09-17
Java(TM) SE Runtime Environment (build 25+37-2486)
Java HotSpot(TM) 64-Bit Server VM (build 25+37-2486, mixed mode, sharing)

javac 25
```

### Opción 2: Usar Eclipse Temurin (Alternativa Gratuita)

Si prefieres una distribución gratuita de OpenJDK:

1. Ve a: https://adoptium.net/temurin/releases/?version=25
2. Descarga **Eclipse Temurin 25 JDK** (Windows x64)
3. Ejecuta el instalador
4. Configura `JAVA_HOME` como se indica arriba

### Opción 3: Usar SDKMAN (Recomendado para Desarrolladores)

Si tienes SDKMAN instalado:

```bash
sdk install java 25.0.0-oracle
sdk default java 25.0.0-oracle
```

## ✔️ Verificar la Instalación

Después de instalar, verifica que todo funciona:

```powershell
# Abre una nueva terminal PowerShell y ejecuta:
java -version
javac -version

# Debería mostrar versión 25 en ambos comandos
```

## 🔧 Compilar el Proyecto

Una vez instalado Java 25 JDK:

```powershell
cd C:\Users\juanj\git\Contabilidad\contabilidad
.\mvnw.cmd clean compile -DskipTests
```

## 🆘 Si Aún Tienes Problemas

### Solución 1: Limpiar caché de Maven

```powershell
# Elimina la carpeta local de Maven
Remove-Item -Recurse -Force $env:USERPROFILE\.m2\repository

# Reintenta la compilación
.\mvnw.cmd clean compile -DskipTests
```

### Solución 2: Verificar JAVA_HOME

```powershell
# Verifica que JAVA_HOME esté configurado correctamente
echo $env:JAVA_HOME

# Debería mostrar: C:\Program Files\Java\jdk-25 (o similar)

# Si no está configurado, ejecuta:
[Environment]::SetEnvironmentVariable('JAVA_HOME', 'C:\Program Files\Java\jdk-25', 'User')
```

### Solución 3: Usar Maven Explícitamente

```powershell
cd C:\Users\juanj\git\Contabilidad\contabilidad

# Ejecuta con la ruta completa a Java 25
$env:JAVA_HOME = 'C:\Program Files\Java\jdk-25'
.\mvnw.cmd clean compile -DskipTests
```

## 🎉 Próximos Pasos

Una vez que la compilación sea exitosa:

```powershell
# 1. Compilar y empaquetar
.\mvnw.cmd clean package

# 2. Ejecutar la aplicación
.\mvnw.cmd spring-boot:run

# 3. Acceder en el navegador
# http://localhost:8080
```

## 📚 Diferencia entre JRE y JDK

| Característica | JRE | JDK |
|---|---|---|
| **Ejecutar aplicaciones Java** | ✅ Sí | ✅ Sí |
| **Compilar código Java** | ❌ No | ✅ Sí |
| **Herramientas de desarrollo** | ❌ No | ✅ Sí (javac, jdb, etc.) |
| **Tamaño** | ~150 MB | ~300 MB |
| **Uso** | Usuarios finales | Desarrolladores |

## 💡 Notas Importantes

- **Java 25 es la versión más nueva**: Asegúrate de descargar la versión 25, no versiones anteriores
- **Reinicia tu terminal**: Después de cambiar variables de entorno, cierra y reabre PowerShell
- **Elimina JRE antiguo**: Es opcional, pero recomendado eliminar el JRE anterior si no lo necesitas
- **Maven wrapper**: El proyecto usa `mvnw` que descargará automáticamente Maven 3.9.6

---

**Una vez completados estos pasos, tu proyecto debería compilar sin problemas.** ✅
