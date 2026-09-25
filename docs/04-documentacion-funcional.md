# Documentación funcional

## Objetivo

FixCampus permite registrar y dar seguimiento a incidencias de infraestructura y servicios dentro de un campus universitario.

## Roles

| Rol | Permisos principales |
|---|---|
| `REPORTER` | Crear reportes, ver sus reportes, comentar y confirmar el cierre |
| `TECHNICIAN` | Ver reportes asignados, comentar y actualizar el avance |
| `ADMIN` | Gestionar usuarios y catálogos, asignar técnicos y consultar el dashboard |

## Reglas de negocio

1. Un correo identifica una sola cuenta.
2. Las cuentas nuevas siempre empiezan como `REPORTER`.
3. Solo se pueden usar categorías y áreas activas.
4. Un reporte nuevo puede editarse o eliminarse por su reportante.
5. La asignación y los cambios de estado requieren permisos según el rol.
6. Resolver una incidencia requiere describir la solución.
7. Cada transición se registra en el historial.

## Evidencias de funcionamiento

- Registro: `POST /api/auth/register`.
- Login: `POST /api/auth/login`.
- Dashboard: `GET /api/dashboard`.
- CRUD de reportes: `/api/reports`.
- Comentarios: `/api/reports/{reportId}/comments`.
- Documentación interactiva: `/swagger-ui/index.html`.
