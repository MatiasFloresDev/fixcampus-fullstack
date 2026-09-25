# Documentación técnica

## Estructura

### Backend

- Java 17 y Spring Boot.
- Spring Web para los controladores REST.
- Spring Data JPA para persistencia.
- Spring Security con sesión HTTP y protección CSRF.
- H2 para ejecución local y PostgreSQL en Render.
- OpenAPI/Swagger para probar los endpoints.

El código está separado en `entities`, `repository`, `service`, `controller`, `dto`, `config` y `exception`. Cada entidad principal tiene su controlador CRUD correspondiente.

### Frontend

- Angular standalone.
- Angular Material para formularios y componentes.
- `Api` centraliza las peticiones y la sesión.
- Guards evitan entrar a dashboard, reportes o administración sin autenticación.
- El proxy local envía `/api` a `http://localhost:8081`.

## Contrato de registro

### Request

```json
{
  "name": "Ana Torres",
  "email": "ana@fixcampus.local",
  "password": "CampusSeguro-2026"
}
```

### Response `201 Created`

```json
{
  "id": 12,
  "name": "Ana Torres",
  "email": "ana@fixcampus.local",
  "role": "REPORTER",
  "active": true
}
```

## Validación realizada

- `npm run build` del frontend: correcto.
- `mvn test` del backend: 4 pruebas correctas.
- Prueba agregada para registrar una cuenta, rechazar correo repetido e iniciar sesión con la cuenta creada.
