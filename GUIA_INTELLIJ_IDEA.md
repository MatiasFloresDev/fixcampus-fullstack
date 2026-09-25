# Ejecutar FixCampus en IntelliJ IDEA

## Requisitos

- IntelliJ IDEA Community o Ultimate.
- JDK 17 configurado en **File > Project Structure > SDK**.
- Node.js 20.19 o superior y npm.
- Maven 3.9+ (o el Maven Wrapper si el equipo lo agrega).

## Backend Spring Boot

1. Abre IntelliJ y selecciona **Open**.
2. Elige la carpeta `backend` del proyecto descomprimido.
3. IntelliJ detectará `pom.xml`; selecciona **Load Maven Project**.
4. Abre `FixCampusApplication.java` y pulsa el botón verde **Run**.
5. Antes de ejecutar, crea estas variables de entorno en la configuración de ejecución:

```text
SEED_ENABLED=true
SEED_PASSWORD=una-clave-local-de-al-menos-12-caracteres
```

La API queda en `http://localhost:8080` y Swagger en `http://localhost:8080/swagger-ui.html`.

## Frontend Angular

1. En IntelliJ abre la pestaña **Terminal**.
2. Entra a `frontend` y ejecuta:

```powershell
npm ci
npm start
```

Abre `http://localhost:4200`. El archivo `proxy.conf.json` envía `/api` al backend.

## Landing

Abre una segunda pestaña de Terminal y ejecuta:

```powershell
cd landing
python -m http.server 5500 --bind 127.0.0.1
```

Abre `http://localhost:5500`. El botón **Ingresar a FixCampus** dirige al frontend Angular.

## Orden para la demostración

1. Ejecuta primero el backend desde IntelliJ.
2. Ejecuta después `npm start` para Angular.
3. Levanta la landing con Python.
4. Usa las cuentas de demostración indicadas en `backend/README.md`.
5. Crea un reporte, asígnalo, cambia su estado, registra la solución y muestra el dashboard.

IntelliJ es el entorno principal para el backend; las dos terminales integradas permiten ejecutar Angular y la landing sin instalar otro editor.
