# Historias de usuario

## HU-01. Crear una cuenta

**Como** estudiante o miembro de la comunidad UPC  
**quiero** crear una cuenta con mi nombre, correo y contraseña  
**para** registrar incidencias y consultar su atención.

### Criterios de aceptación

- El nombre y el correo son obligatorios.
- La contraseña tiene entre 12 y 72 caracteres.
- El correo no puede estar registrado previamente.
- La cuenta se crea con rol `REPORTER` y queda activa.
- Después del registro, el usuario puede iniciar sesión.

## HU-02. Registrar una incidencia

**Como** usuario autenticado  
**quiero** registrar el problema, ubicación, categoría y prioridad  
**para** que el equipo del campus pueda atenderlo.

### Criterios de aceptación

- El formulario valida los campos obligatorios.
- El reporte inicia en estado `NEW`.
- El usuario puede revisar el reporte en el listado.

## HU-03. Dar seguimiento

**Como** persona que reportó una incidencia  
**quiero** consultar el estado, historial y comentarios  
**para** saber qué acciones se realizaron.

### Criterios de aceptación

- Se muestran los estados `NEW`, `ASSIGNED`, `IN_PROGRESS`, `RESOLVED` y `CLOSED`.
- El detalle muestra fechas, responsable, historial y notificaciones.
- Solo el usuario autorizado puede editar o comentar su reporte.

## HU-04. Gestionar la atención

**Como** administrador o técnico  
**quiero** asignar, comentar y cambiar el estado de una incidencia  
**para** organizar el trabajo del campus.

### Criterios de aceptación

- El administrador puede asignar un técnico.
- El técnico puede avanzar el estado siguiendo el flujo permitido.
- El cierre exige una nota de solución.
