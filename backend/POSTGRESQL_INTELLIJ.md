# Conectar FixCampus con PostgreSQL desde IntelliJ IDEA

## 1. Crear la base

En pgAdmin (o `psql`) crea una base llamada `fixcampus`. Puedes usar el usuario `postgres` durante la práctica:

```sql
CREATE DATABASE fixcampus;
```

No es necesario crear tablas: Hibernate las crea al primer arranque porque `DDL_AUTO=update`.

## 2. Configurar IntelliJ

1. Abre **Run > Edit Configurations...** y selecciona `FixCampusApplication`.
2. En **Environment variables** agrega:

```text
DB_URL=jdbc:postgresql://localhost:5432/fixcampus;DB_USERNAME=postgres;DB_PASSWORD=TU_CLAVE_POSTGRES;DDL_AUTO=update;SEED_ENABLED=true;SEED_PASSWORD=una-clave-local-de-al-menos-12-caracteres
```

3. Pulsa **Apply** y ejecuta nuevamente la aplicación.
4. En los logs debe aparecer una conexión PostgreSQL y no una URL `jdbc:h2`.

El driver ya está declarado en `backend/pom.xml`. Si Maven solicita descargarlo, pulsa **Load Maven Changes** y espera a que termine.

## 3. Verificar en IntelliJ

La ventana **Database** de IntelliJ es solo un cliente para inspeccionar la base. Crea una conexión `+ > Data Source > PostgreSQL` con host `localhost`, puerto `5432`, base `fixcampus`, usuario `postgres` y tu clave. Pulsa **Download missing driver** y luego **Test Connection**.

Después del primer arranque aparecerán las tablas `app_user`, `report`, `category`, `area`, `technician`, `report_history` y `notification_log`.

## Si quieres continuar con H2

No cambies nada: si no defines `DB_URL`, FixCampus usa `jdbc:h2:file:./data/fixcampus`. H2 sirve para demostrar el proyecto sin instalar PostgreSQL; PostgreSQL es la configuración recomendada para la entrega final.
