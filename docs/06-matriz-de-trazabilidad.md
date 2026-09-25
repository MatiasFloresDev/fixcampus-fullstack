# Matriz de trazabilidad

| Requisito | Pantalla | Endpoint | Evidencia |
|---|---|---|---|
| Crear cuenta | `/register` | `POST /api/auth/register` | Prueba de registro y login |
| Iniciar sesión | `/login` | `POST /api/auth/login` | Sesión HTTP con CSRF |
| Ver resumen | `/dashboard` | `GET /api/dashboard` | Tarjetas de métricas y gráficos |
| Listar incidencias | `/reports` | `GET /api/reports` | Filtros por estado, categoría, área y prioridad |
| Crear incidencia | `/reports/new` | `POST /api/reports` | Validación y estado inicial `NEW` |
| Consultar detalle | `/reports/:id` | `GET /api/reports/{id}` | Historial, comentarios y notificaciones |
| Comentar | `/reports/:id` | `/api/reports/{reportId}/comments` | Alta, edición y eliminación |
| Administrar catálogos | `/admin` | `/api/categories`, `/api/areas`, `/api/roles`, `/api/report-statuses`, `/api/priorities` | CRUD protegido por rol |

La matriz sirve para explicar que cada pantalla tiene una necesidad concreta y un endpoint verificable en Swagger.
