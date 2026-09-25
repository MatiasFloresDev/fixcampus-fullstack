# Código de FixCampus

## 1. Landing

- `landing/index.html`: portada, propuesta de valor, ODS 11, beneficios, preguntas frecuentes y botones.
- `landing/styles.css`: diseño responsive para móvil y escritorio.
- `landing/app.js`: menú móvil, selector español/inglés, modales de privacidad/términos y botones hacia la aplicación.
- `landing/config.js`: URL del frontend (`http://localhost:4200` por defecto).

La landing no necesita backend: su función es presentar el producto y dirigir al sistema.

## 2. Frontend

Angular consume el backend por `/api` mediante `src/app/api.service.ts`.

- `login.component.ts`: inicio y cierre de sesión.
- `reports.component.ts` y `report-form.component.ts`: listado, filtros y CRUD de reportes.
- `report-detail.component.ts`: historial, asignación y transición de estados.
- `dashboard.component.ts`: indicadores y distribuciones.
- `admin.component.ts`: usuarios, categorías, áreas y técnicos.
- `src/main.ts`: rutas y guardas de sesión/rol.

## 3. Backend y APIs

| Módulo | Endpoints principales |
|---|---|
| Sesión | `GET /api/auth/csrf`, `POST /api/auth/login`, `GET /api/auth/me`, `POST /api/auth/logout` |
| Reportes | `GET/POST /api/reports`, `GET/PUT/DELETE /api/reports/{id}`, `POST /assign`, `POST /transition` |
| Catálogos | `GET/POST/PUT /api/categories`, `/api/areas`, `/api/technicians` |
| Administración | `GET/POST/PUT /api/users` |
| IA | `POST /api/ai/suggest` |
| Dashboard | `GET /api/dashboard` |

La API valida roles, propiedad del reporte, CSRF, transiciones permitidas y nota obligatoria al resolver. La IA y correo son adaptadores configurables por variables de entorno; sin credenciales, la IA responde `MANUAL` de forma transparente.

## Ejecución local

1. Backend: `SEED_ENABLED=true`, `SEED_PASSWORD=una-clave-local-de-al-menos-12-caracteres`, `mvn spring-boot:run`.
2. Frontend: `npm ci`, `npm start`.
3. Landing: `python -m http.server 5500 --bind 127.0.0.1` dentro de `landing`.

Las cuentas de demostración y el flujo completo están descritos en los README de cada carpeta.
