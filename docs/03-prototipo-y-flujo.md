# Prototipo y flujo de pantallas

El prototipo navegable está en [`prototipo-fixcampus.html`](./prototipo-fixcampus.html). Se usó como referencia para separar la experiencia pública de la zona autenticada.

## Pantallas principales

| Pantalla | Objetivo | Entrada | Salida |
|---|---|---|---|
| Inicio de sesión | Validar acceso | Correo y contraseña | Dashboard |
| Crear cuenta | Registrar un reportante | Nombre, correo y contraseña | Inicio de sesión |
| Dashboard | Mostrar resumen | Sesión activa | Métricas y accesos |
| Incidencias | Consultar y filtrar | Filtros por estado, área y prioridad | Detalle |
| Nueva incidencia | Crear un reporte | Datos del problema | Detalle del reporte |
| Detalle | Ver seguimiento | Id del reporte | Historial y comentarios |

## Decisiones de interfaz

- La pantalla de acceso tiene un formulario corto y un enlace visible para crear cuenta.
- El dashboard concentra los indicadores antes de mostrar tablas.
- Los estados usan etiquetas de color y texto para no depender solo del color.
- El formulario de incidencia mantiene los campos en un orden natural: problema, ubicación, clasificación y prioridad.
- El diseño se adapta a pantallas pequeñas y conserva el foco de teclado.
