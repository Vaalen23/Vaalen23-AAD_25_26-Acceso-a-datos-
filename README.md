AAD – Acceso a Datos

Proyecto desarrollado para la asignatura Acceso a Datos (2º DAM) utilizando Spring Boot, JDBC y PostgreSQL.

Descripción

Esta aplicación de consola permite gestionar alumnos, módulos y matrículas en una base de datos PostgreSQL.

El proyecto está estructurado por capas (configuración, repositorio, servicio, modelo) y trabaja con JDBC puro, sin utilizar ningún ORM como JPA o Hibernate.
También incluye gestión manual de transacciones (commit y rollback) y uso de logging profesional con SLF4J.

La aplicación se ejecuta automáticamente al arrancar gracias a CommandLineRunner.

Estructura del Proyecto
src/main/java/com/Valentin/aad

application  → Clase principal (AadApplication)
config       → Configuración JDBC y transacciones
model        → Entidades (Student, Module, etc.)
repository   → Acceso a datos
service      → Lógica de negocio

Scripts SQL:

src/main/resources/ddl

01_schema.sql
02_procedures.sql
Tecnologías utilizadas

Java 25

Spring Boot 3.5.6

PostgreSQL

JDBC

Maven

SLF4J + Logback

Base de Datos

Configuración utilizada en application.yml:

Host: localhost

Puerto: 5433

Base de datos: aad_db

Usuario y contraseña configurados localmente

Los scripts SQL se ejecutan automáticamente al iniciar la aplicación.

Modo Reset

El archivo 01_schema.sql incluye sentencias DROP TABLE IF EXISTS para reiniciar la base de datos en cada ejecución.

Esto evita errores por claves únicas duplicadas (nif o email) cuando se ejecuta varias veces el programa.

Funcionalidades implementadas

Inserción de alumnos

Inserción de módulos

Matrícula simple

Matrícula múltiple con transacción manual

Commit y rollback

Conteo de matrículas

Logging estructurado

Ejemplo de salida en consola:

Transacción iniciada (autoCommit=false)
Commit realizado
Matrícula completada OK
Total matrículas del alumno: 2

Cómo ejecutar el proyecto

Tener PostgreSQL ejecutándose.

Comprobar que la configuración en application.yml es correcta.

Ejecutar la clase:

AadApplication.java

La aplicación se ejecutará como programa de consola y finalizará automáticamente.

Conceptos trabajados

Arquitectura por capas

JDBC sin ORM

Gestión manual de transacciones

Inyección de dependencias

Control de errores

Restricciones UNIQUE

Integridad referencial

Autor

Valentín Urdillo Fernández
2º DAM
