# ACT 3.1 - Migracion de gestion academica de JDBC a JPA

Autor: Valentin Urdillo Fernandez  
Asignatura: Acceso a Datos  
Curso: 2025/2026

## Objetivo

Esta actividad migra la capa de persistencia de una aplicacion academica desde JDBC manual a Spring Data JPA con Hibernate y HikariCP.

El dominio mantiene alumnos, modulos y matriculas. La aplicacion crea las tablas automaticamente desde las entidades y usa repositorios JPA para guardar, consultar, modificar y borrar datos.

## Tecnologias usadas

- Java 17
- Spring Boot 3.5.6
- Spring Data JPA
- Hibernate
- PostgreSQL
- HikariCP
- Lombok
- Jakarta Validation
- Docker Compose
- Maven

## Ramas de trabajo

La rama de entrega debe ser:

```bash
git checkout feature/act_2_2
git checkout -b feature/act_3_1
```

## Base de datos con Docker

Para levantar PostgreSQL:

```bash
docker compose up -d
```

Datos usados por la aplicacion:

```yaml
url: jdbc:postgresql://localhost:5433/aad_db
username: user
password: pass
```

El puerto externo es 5433 para evitar conflictos con otros PostgreSQL locales.

## Configuracion JPA

En `application.yml` se configura:

```yaml
spring:
  jpa:
    hibernate:
      ddl-auto: create-drop
    show-sql: true
    properties:
      hibernate:
        format_sql: true
```

Con `ddl-auto: create-drop`, Hibernate crea el esquema al arrancar la aplicacion y lo elimina al cerrar. Esto sirve para pruebas de clase porque no hacen falta scripts SQL externos.

HikariCP queda configurado con un maximo de 5 conexiones. Su funcion es mantener conexiones reutilizables con la base de datos, evitando abrir y cerrar una conexion manual en cada operacion como pasaba con JDBC.

## Estructura del proyecto

```text
src/main/java/com/valentin/aad
├── AadApplication.java
├── config
├── exception
├── model
│   ├── Enrollment.java
│   ├── Module.java
│   ├── Profile.java
│   └── Student.java
├── repository
│   ├── EnrollmentRepository.java
│   ├── ModuleRepository.java
│   └── StudentRepository.java
└── service
    ├── EnrollmentService.java
    ├── ModuleService.java
    ├── StudentManagementService.java
    └── StudentService.java
```

## Entidades y relaciones

### Student

Representa a un alumno. Tiene NIF, nombre, email, curso, perfil y una lista de matriculas.

Relaciones:

- `@OneToOne` con `Profile`.
- `@OneToMany` con `Enrollment`.

### Module

Representa un modulo academico. Tiene codigo, nombre y horas.

Relacion:

- `@OneToMany` con `Enrollment`.

### Enrollment

Representa la matricula de un alumno en un modulo. Es la tabla intermedia con atributos propios.

Campos principales:

- Alumno.
- Modulo.
- Fecha de matricula.
- Nota final.

Relaciones:

- `@ManyToOne` con `Student`.
- `@ManyToOne` con `Module`.

La combinacion `student_id` + `module_id` es unica para evitar que un alumno se matricule dos veces en el mismo modulo.

## Repositorios JPA

Los repositorios extienden `JpaRepository`, por lo que ya tienen operaciones CRUD sin escribir SQL manual:

- `save()`
- `findAll()`
- `findById()`
- `deleteById()`
- `existsById()`

Tambien se incluyen consultas derivadas y JPQL:

```java
Optional<Student> findByNif(String nif);
List<Student> findByEmailContainingIgnoreCase(String email);
```

```java
@Query("SELECT e FROM Enrollment e WHERE e.finalGrade >= :minGrade ORDER BY e.finalGrade DESC")
List<Enrollment> findByMinFinalGrade(@Param("minGrade") Double minGrade);
```

Uso `@Query` cuando la consulta necesita mas control, condiciones sobre relaciones u ordenacion. Para busquedas sencillas uso metodos derivados porque son mas rapidos de leer.

## Transacciones

Las transacciones se gestionan en la capa de servicio con `@Transactional`.

Ejemplo:

```java
@Transactional
public Enrollment enrollStudent(Long studentId, Long moduleId, Double finalGrade) {
    ...
}
```

Si durante una matricula ocurre un error, Spring realiza rollback y no deja datos a medias.

Tambien hay metodos de solo lectura con:

```java
@Transactional(readOnly = true)
```

Esto deja claro que el metodo solo consulta datos y no modifica la base de datos.

## Validaciones y excepciones

Las entidades usan validaciones como:

- `@NotBlank`
- `@NotNull`
- `@Email`
- `@Positive`
- `@DecimalMin`
- `@DecimalMax`

Tambien se han creado excepciones propias:

- `StudentNotFoundException`
- `ModuleNotFoundException`
- `EnrollmentNotFoundException`

Esto permite mostrar errores mas claros cuando un registro no existe.

## Datos de prueba

Al arrancar la aplicacion se insertan datos desde `CommandLineRunner`:

- Valentin
- vito
- kiko
- paco
- carlos

Tambien se crean modulos y matriculas para comprobar las relaciones, las consultas JPQL y las transacciones.

## Logs de Hibernate

La aplicacion muestra las sentencias generadas por Hibernate:

```yaml
logging:
  level:
    org.hibernate.SQL: DEBUG
    org.hibernate.orm.jdbc.bind: TRACE
```

Esto permite comprobar que JPA genera las consultas correctamente y ayuda a detectar problemas como demasiadas consultas al cargar relaciones.

## Problema N+1

Las relaciones se cargan con `FetchType.LAZY` para no traer informacion innecesaria. Cuando necesito cargar relaciones concretas, uso `@EntityGraph`, por ejemplo en `findAllWithProfileAndEnrollments()` o `findAllWithStudentAndModule()`.

Asi se evita lanzar muchas consultas pequeñas cuando se necesita consultar un bloque de datos relacionado.

## Como ejecutar

1. Levantar PostgreSQL:

```bash
docker compose up -d
```

2. Ejecutar el proyecto desde IntelliJ o con Maven:

```bash
mvn spring-boot:run
```

3. Comprobar en consola:

- Creacion de tablas por Hibernate.
- Insercion de alumnos, modulos y matriculas.
- Consultas JPQL.
- Prueba de rollback.

## Diferencia frente a JDBC

Antes, con JDBC, habia que escribir SQL manual, crear conexiones, preparar sentencias y mapear resultados.

Ahora, con JPA, se trabaja con objetos Java. Hibernate genera el SQL y Spring Data JPA proporciona los repositorios. El codigo queda mas limpio, mas facil de mantener y con menos riesgo de errores repetitivos.

## Reflexion final

La migracion a JPA mejora bastante la forma de trabajar con bases de datos. El codigo se centra mas en el modelo academico y menos en abrir conexiones o escribir consultas repetidas. Ademas, las transacciones, validaciones y relaciones quedan mejor organizadas dentro de la aplicacion.
