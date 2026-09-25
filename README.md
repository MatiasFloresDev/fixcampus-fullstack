# FixCampus

FixCampus es un proyecto académico para registrar y dar seguimiento a incidencias dentro de un campus. Lo desarrollé para practicar el trabajo entre una aplicación web y una API REST.

## Tecnologías

- Angular y Angular Material
- Java con Spring Boot
- PostgreSQL y JPA
- Swagger / OpenAPI

## Funciones principales

- Registro e inicio de sesión por roles
- Creación y seguimiento de incidencias
- Asignación de técnicos
- Cambios de estado e historial
- Panel con métricas
- Administración de usuarios, categorías y áreas

## Cómo ejecutarlo

El backend necesita Java 17, Maven y PostgreSQL. Antes de iniciarlo se configuran estas variables:

```powershell
$env:DB_PASSWORD='tu-clave-de-postgresql'
$env:SEED_ENABLED='true'
$env:SEED_PASSWORD='una-clave-local-de-al-menos-12-caracteres'
mvn spring-boot:run
```

El backend queda en `http://localhost:8080` y Swagger en `http://localhost:8080/swagger-ui.html`.

En otra terminal, dentro de `frontend`:

```powershell
npm ci
npm start
```

La aplicación web queda en `http://localhost:4200`.
