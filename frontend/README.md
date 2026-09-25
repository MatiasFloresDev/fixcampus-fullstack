# Frontend FixCampus

SPA Angular 20.3 + Angular Material 20.2, TypeScript 5.9 y RxJS 7.8. Node compatible: 20.19+, 22.12+ o 24.x. Las versiones resueltas se fijan en `package-lock.json`.

## Ejecutar

Desde esta carpeta, con Node y npm instalados:

```bash
npm ci
npm start
```

Abrir http://localhost:4200. El backend debe ejecutarse en http://localhost:8080. `proxy.conf.json` reenvía `/api` y conserva las cookies en el mismo origen. No usar `file://` para la SPA.

```bash
npm run build
npm run check
```

El build productivo queda en `dist/fixcampus/browser`. En despliegue configure el servidor para devolver `index.html` en rutas del frontend y para reenviar `/api` al backend; use HTTPS.

## Organización para estudiar

- `src/main.ts`: arranque, proveedores y rutas con guardas de sesión/administrador.
- `src/app/api.service.ts`: HTTP, sesión y CSRF. El backend decide la autorización final.
- `models.ts`: DTO del contrato REST; no son entidades persistentes.
- `i18n.service.ts`: textos es_419/en_US y fechas; solo la preferencia de idioma se conserva localmente.
- `login`, `dashboard`, `reports`, `report-form`, `report-detail`, `admin`: componentes standalone por pantalla. Los formularios usan FormsModule y los controles usan Angular Material.
- `styles.css`: diseño responsive compartido. Sin fuentes ni iconos descargados de terceros.

Todos los datos proceden del backend. No hay modo de datos ficticios, contraseñas fijas ni claves de IA en el cliente. El proveedor IA se solicita explícitamente y sus sugerencias requieren confirmación antes de aplicarse. `MANUAL` se presenta como registro manual. No se oculta la indisponibilidad del servidor.

## Flujos

Reportante: crear, consultar, filtrar, editar/eliminar reporte propio NUEVO y cerrar RESUELTO. Técnico: consultar asignaciones, iniciar atención y resolver con nota. Administrador: panel, catálogos y usuarios, asignación/reasignación y transiciones autorizadas. La edición/eliminación de reportes solo aparece en NUEVO. Los catálogos se desactivan para conservar referencias históricas.

El dashboard muestra total, abiertos, resueltos, cerrados, promedio de resolución y distribución por estado, categoría, área y mes. Las notificaciones de cada reporte reflejan el estado registrado por el backend, no presumen entrega.

Los textos de interfaz son bilingües. El contenido que ingresan usuarios y los mensajes enviados por el backend conservan su idioma original. Incluye etiquetas de formulario, mensajes de error, enlace de salto, foco visible, navegación de teclado, estados vacíos y carga. Esto no equivale a una auditoría de conformidad WCAG.

## Comprobación manual integrada

1. Arrancar backend con semillas habilitadas y una `SEED_PASSWORD` propia; iniciar sesión con las cuentas descritas en el README del backend.
2. Crear reporte con el reportante. Solicitar IA sin configurar proveedor: debe aparecer MANUAL, sin afirmar una respuesta de IA.
3. Editar y eliminar un reporte NUEVO; crear otro para conservarlo.
4. Entrar como administrador, asignar técnico. Entrar como técnico, iniciar atención y resolver con una nota.
5. Volver al reportante y cerrar. Comprobar historial y actualización del dashboard.
6. Probar usuarios, categorías, áreas y técnicos: crear, editar y desactivar.
7. Cambiar idioma, usar teclado y probar ancho móvil de 390 px. Verificar filtros sin resultados y servidor detenido.

La compilación estricta comprueba TypeScript y templates; no reemplaza estas verificaciones contra el backend real.
