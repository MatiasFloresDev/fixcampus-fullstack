# FixCampus entrega técnica

Este paquete contiene los tres productos que se deben demostrar y entregar:

| Producto | Carpeta | Tecnología | Cómo ejecutarlo |
|---|---|---|---|
| Landing page | `landing` | HTML5, CSS3, JavaScript | `python -m http.server 5500 --bind 127.0.0.1` |
| Aplicación web | `frontend` | Angular 20, TypeScript, Angular Material | `npm ci` y `npm start` |
| API REST | `backend` | Java 17, Spring Boot 3.5, JPA, H2 | `mvn spring-boot:run` |

## Orden para la demostración

1. Inicia el backend en `http://localhost:8080` con `SEED_ENABLED=true` y una contraseña local de al menos 12 caracteres.
2. Inicia el frontend en `http://localhost:4200`; el proxy envía `/api` al backend y mantiene la cookie de sesión.
3. Inicia el landing en `http://localhost:5500`; el botón principal abre la aplicación.
4. Ingresa como reportante, crea una incidencia y consulta su estado.
5. Ingresa como administrador, asigna un técnico.
6. Ingresa como técnico, inicia atención y resuelve agregando una nota.
7. Regresa como reportante y confirma el cierre.
8. Muestra historial, dashboard, filtros, cambio de idioma y rechazo de acceso a un reporte ajeno.

## Integraciones

La IA se configura en el backend con `AI_API_KEY`, `AI_MODEL` y `AI_URL`. La API valida categoría, prioridad, título y resumen. Si no hay proveedor o falla la respuesta, retorna `source=MANUAL`; la interfaz lo muestra como clasificación manual. El correo usa `RESEND_API_KEY`, `MAIL_FROM` y el destinatario de prueba. Una falla de correo no revierte la asignación.

No se incluyen claves en el repositorio. Las entrevistas, capturas de Trello/Figma, URLs públicas, commits y correos reales deben generarse por el equipo y anexarse al informe.

## Revisión técnica

El backend incluye pruebas de integración de sesión, CSRF, propiedad de reportes, transiciones, historial, fallback de IA y dashboard. Si Maven no encuentra dependencias, ejecuta `mvn test` con conexión a Maven Central desde una instalación local. En el entorno de elaboración, la política de red impidió descargar el POM padre de Spring Boot.

La compilación Angular puede fallar dentro del sandbox de Codex por restricciones de acceso a rutas del proyecto. En una terminal local, ejecuta `npm ci` y `npm run build`; cualquier error restante debe corregirse antes de presentar la aplicación como compilada.
