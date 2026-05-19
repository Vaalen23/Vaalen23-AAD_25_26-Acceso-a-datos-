# ACT 1.5 - Gestor de Logs

Aplicacion Java para gestionar un fichero de logs llamado `app.log`.

## Funcionamiento

La aplicacion permite:

- Anadir eventos al fichero `app.log`.
- Guardar cada evento con fecha y hora usando el formato `[YYYY-MM-DD HH:mm:ss] Mensaje`.
- Leer todos los eventos guardados.
- Filtrar eventos por una fecha concreta usando el formato `YYYY-MM-DD`.
- Trabajar con codificacion UTF-8.
- Cambiar la codificacion a ISO-8859-1.
- Gestionar errores de lectura, escritura, fecha incorrecta y mensajes vacios.

## Requisitos

- Java 17 o superior.
- Maven o Maven Wrapper incluido en el proyecto.
- IntelliJ IDEA, Eclipse, NetBeans o cualquier IDE compatible con Maven.

## Ejecucion en IntelliJ IDEA

1. Abrir la carpeta del proyecto.
2. Esperar a que Maven cargue las dependencias.
3. Activar annotation processing si IntelliJ lo solicita por Lombok.
4. Ejecutar la clase `AadApplication`.

## Ejecucion por terminal

En Windows:

```bash
mvnw.cmd spring-boot:run
```

En Linux o macOS:

```bash
./mvnw spring-boot:run
```

## Menu de la aplicacion

```text
=== GESTOR DE LOGS ===
Codificacion actual: UTF-8
1. Anadir evento
2. Filtrar eventos por fecha
3. Mostrar todos los eventos
4. Cambiar codificacion
5. Cargar eventos de ejemplo
0. Salir
```

## Ejemplos de uso

### Anadir evento

Entrada:

```text
Usuario Valentin inicio sesion
```

Salida en `app.log`:

```text
[2026-05-19 18:45:00] Usuario Valentin inicio sesion
```

### Filtrar por fecha

Entrada:

```text
2026-05-19
```

Salida:

```text
[2026-05-19 18:45:00] Usuario Valentin inicio sesion
[2026-05-19 18:46:00] Usuario Paco consulto los registros
[2026-05-19 18:47:00] Usuario Vito modifico la configuracion
[2026-05-19 18:48:00] Usuario Ruben filtro eventos por fecha
[2026-05-19 18:49:00] Usuario Carlos cerro sesion
[2026-05-19 18:50:00] Usuario Kiko genero una copia del log
```

## Estructura principal

```text
src/main/java/com/example/aad
├── AadApplication.java
├── model
│   └── EventoLog.java
├── repository
│   └── LogRepository.java
├── service
│   ├── CodificacionService.java
│   ├── GestorService.java
│   └── LogService.java
└── util
    └── Constantes.java
```

## Fichero generado

El fichero principal es:

```text
app.log
```

Este fichero se crea automaticamente si no existe.
