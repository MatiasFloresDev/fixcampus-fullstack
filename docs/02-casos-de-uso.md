# Casos de uso

## CU-01. Registrarse

**Actor:** visitante  
**Precondición:** no tiene una sesión activa.  
**Resultado:** se crea una cuenta activa con rol `REPORTER`.

1. El visitante abre “Crear cuenta”.
2. Ingresa nombre, correo, contraseña y confirmación.
3. El frontend valida formato y longitud.
4. El sistema obtiene el token CSRF.
5. El sistema registra la cuenta en `POST /api/auth/register`.
6. Si el correo está disponible, devuelve `201 Created`.
7. El visitante vuelve a la pantalla de inicio de sesión.

**Excepciones:** correo repetido (`409`), datos inválidos (`400`) o servidor no disponible.

## CU-02. Iniciar sesión

**Actor:** usuario registrado  
**Resultado:** se crea una sesión HTTP y se muestra el dashboard.

1. El usuario ingresa correo y contraseña.
2. El frontend solicita el token CSRF.
3. Envía `POST /api/auth/login`.
4. El backend valida credenciales y guarda la sesión.
5. El frontend carga `/dashboard`.

## CU-03. Registrar incidencia

**Actor:** usuario con rol `REPORTER`  
**Resultado:** incidencia creada y visible en el listado.

1. El usuario selecciona “Nueva incidencia”.
2. Completa título, descripción, ubicación, categoría, área y prioridad.
3. El sistema valida que las referencias estén activas.
4. Envía `POST /api/reports`.
5. La incidencia se crea con estado `NEW`.

## CU-04. Resolver incidencia

**Actor:** administrador y técnico  
**Resultado:** incidencia cerrada con historial.

1. El administrador asigna un técnico.
2. El técnico registra el avance.
3. El técnico cambia a `RESOLVED` con una nota.
4. El reportante confirma y el reporte pasa a `CLOSED`.
5. Cada cambio queda en el historial.
