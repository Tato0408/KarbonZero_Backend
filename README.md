# KarbonZero — API Institucional

> API REST que permite a una **empresa o institución** afiliada a **KarbonZero** registrar el consumo de sus recursos (electricidad, combustible, agua, etc.), convertir unidades de medida y calcular la **huella de carbono (CO₂)** que genera, además de administrar su canal dentro de la red KarbonZero.

---

## Contenido

- [Descripción](#descripción)
- [Ecosistema KarbonZero](#ecosistema-karbonzero)
- [Autores](#autores)
- [Stack Tecnológico](#stack-tecnológico)
- [Requisitos previos](#requisitos-previos)
- [Arquitectura del Proyecto](#arquitectura-del-proyecto)
- [Librerías principales y su función](#librerías-principales-y-su-función)
- [Convenciones de nomenclatura](#convenciones-de-nomenclatura)
- [Endpoints de la API](#endpoints-de-la-api)
- [Base de datos](#base-de-datos)
- [Variables de entorno](#variables-de-entorno)
- [Ejecución del Proyecto](#ejecución-del-proyecto)
- [Solución de problemas comunes](#solución-de-problemas-comunes)

---

## Descripción

Cada empresa afiliada despliega su propia instancia de esta API, con su propia base de datos. Desde el panel web institucional ([KarbonZero_Frontend](https://github.com/Tato0408/KarbonZero_Frontend)) el personal de la empresa puede:

- **Recursos** — Registrar los recursos que consume la empresa, su **factor de huella de carbono** y sus distintas **purezas** (variantes del recurso).
- **Medidas y conversiones** — Definir medidas (masa, volumen, energía…), unidades de medida y factores de conversión entre unidades.
- **Consumos** — Registrar cuánto se consumió de cada recurso, en qué fecha y a qué costo, y filtrar los consumos por recurso y rango de fechas.
- **CO₂** — Calcular las emisiones por consumo, por recurso y el total de la empresa.
- **Usuarios y roles** — Administrar las cuentas del personal de la empresa.
- **Canal** — Consultar y editar el canal de la empresa, y aprobar o eliminar sus publicaciones.

La empresa se identifica ante la API central ([KarbonZero_BackendMobile](https://github.com/Tato0408/KarbonZero_BackendMobile)) con su **API Key** y su **dominio**: el login de empresa, los datos de la empresa y todo lo relacionado con el canal se reenvían a esa API.

---

## Ecosistema KarbonZero

| Repositorio | Función | Consume |
|---|---|---|
| [KarbonZero_BackendMobile](https://github.com/Tato0408/KarbonZero_BackendMobile) | API central: app móvil, panel de gestión, empresas y canales | Oracle Database (`Database/KarbonZero.sql`) |
| **KarbonZero_Backend** _(este)_ | API institucional de una empresa (consumo de recursos y CO₂) | Su propia BD (`Database/KarbonZeroInstitucional.sql`) + la API central (`EXTERNAL_ADMIN_DOMAIN`) |
| [KarbonZero_Frontend](https://github.com/Tato0408/KarbonZero_Frontend) | Panel web institucional de la empresa | Esta API + la API central (mapa) |
| [KarbonZeroManagment_Frontend](https://github.com/Tato0408/KarbonZeroManagment_Frontend) | Panel web de gestión (administradores) | API central |
| [KarbonZero_FrontendMobile](https://github.com/Tato0408/KarbonZero_FrontendMobile) | App móvil del usuario final (Framework7 + Capacitor) | API central |

### Ejecución local

Ninguno de los proyectos tiene actualmente un despliegue público activo: el ecosistema completo está preparado para ejecutarse en local. Puertos por defecto:

| Servicio | Repositorio | URL local |
|---|---|---|
| Base de datos (Oracle XE, `XEPDB1`) | Scripts en `KarbonZero_BackendMobile/Database` y `KarbonZero_Backend/Database` | `localhost:1521` |
| API central | KarbonZero_BackendMobile | http://localhost:8080 |
| API institucional | KarbonZero_Backend | http://localhost:8081 |
| Panel de gestión | KarbonZeroManagment_Frontend | http://localhost:5173 |
| Panel institucional | KarbonZero_Frontend | http://localhost:5174 |
| App móvil (navegador) | KarbonZero_FrontendMobile | http://localhost:5175 |

Orden de arranque: **base de datos → API central → API institucional → frontends**.

Cuentas de demostración incluidas en los scripts (contraseña `KarbonZero2026!` en todas):

| Dónde | Correo | Permisos |
|---|---|---|
| Panel de gestión | `gerardoajf@karbonzero.local` | Administrador (`status` 1) |
| App móvil | `freddy@karbonzero.local` | Usuario de la app |
| Panel institucional | `freddy@karbonzero.local` | Lectura y escritura (rol *Coordinador de finanzas*) |
| Panel institucional | `tato@karbonzero.local` | Solo lectura (rol *Coordinador de medio ambiente*) |

---

## Autores

| # | Autor | GitHub |
|---|-------|--------|
| 1 | Oscar Abel Velásquez Joyar | [@OscarAVJ](https://github.com/OscarAVJ) |
| 2 | Camila Mariana Quinteros Gómez | [@camila-mariana](https://github.com/Camila-mariana) |
| 3 | Freddy Ricardo Pérez Alvarenga | [@Tato0408](https://github.com/Tato0408) |
| 4 | Joshua Gilberto Marinero Hernández | [@Joshuaz](https://github.com/Joshuaz) |
| 5 | Gerardo Andrés Jovel Franco | [@GerardoAJF](https://github.com/GerardoAJF) |
| 6 | Edwin Geovanni Alfaro Alfaro | [@EdwinAlfaro123](https://github.com/EdwinAlfaro123) |

---

## Stack Tecnológico

| Capa | Tecnologías |
|------|-------------|
| **Lenguaje y framework** | Java 17, Spring Boot 3.4.7 (Web, Data JPA, Security, Validation, Mail) |
| **Base de datos** | Oracle Database (XE 21c en local) vía `ojdbc11` |
| **Autenticación** | JWT (`jjwt`) en cookie `authToken`, contraseñas con Argon2 |
| **Archivos** | Cloudinary (imágenes de perfil) |
| **Integración** | API central de KarbonZero (`RestTemplate` con `X-API-Key` y `X-Company-Domain`) |
| **Despliegue** | Ejecución local; preparado para Heroku (empaquetado `.war`, ver `Procfile`) |

---

## Requisitos previos

| Herramienta | Para qué |
|---|---|
| JDK 17 | Compilar y ejecutar el proyecto |
| Maven 3.9+ _(o el wrapper `mvnw` incluido)_ | Gestión de dependencias y build |
| Oracle Database XE 21c (o superior) | Base de datos local — ver [Base de datos](#base-de-datos) |
| Empresa registrada en la API central (API Key + dominio) | Login de empresa y canal |
| Cuenta de Cloudinary y de Gmail con contraseña de aplicación | Imágenes y correos de recuperación |

---

## Arquitectura del Proyecto

```
KarbonZero_Backend/
├── src/main/java/project/karbonzero/
│   ├── Config/
│   │   ├── Security/SecurityConfig.java    # Rutas públicas y permisos por rol
│   │   ├── Cors/CorsConfig.java            # Orígenes permitidos
│   │   ├── Cloudinary/CloudinaryConfig.java
│   │   ├── App/AppConfig.java
│   │   └── Argon2PasswordEncoder.java
│   ├── Controllers/                        # Auth, Channel, Cloudinary, Consumption, Measure, Resource, User
│   ├── Services/                           # Lógica de negocio (Auth, PasswordRecovery, Mail, ...)
│   ├── Repositories/                       # Spring Data JPA, por dominio
│   ├── Entities/                           # Consumption, Measure (Measure, MeasureUnit, ConversionUnit),
│   │                                       # Resource (Resource, ResourcePurity), User (User, Rol)
│   ├── Models/DTO/                         # DTOs (incluye Mail/ChannelDTO y MailDTO)
│   ├── Exceptions/                         # BadData, BadId, Create, Read, Update, Delete
│   ├── Utils/                              # JWTUtils, JwtCookieAuthFilter, CodeGenerator, Responses
│   ├── KarbonzeroApplication.java          # Punto de entrada
│   └── ServletInitializer.java
├── src/main/resources/application.properties
├── pom.xml
└── Procfile
```

> **Nota:** el proyecto sigue una **arquitectura en capas** (`Controller → Service → Repository → Entity`), y dentro de cada capa los archivos se agrupan por **dominio de negocio** (`Consumption/`, `Measure/`, `Resource/`, `User/`).

Modelo de datos principal:

```
TBMEASURES ─< TBMEASUREUNITS ─< TBCONVERSIONUNITS
                    │
                    └─< TBRESOURCES ─< TBRESOURCEPURITIES ─< TBCONSUMPTIONS >─ TBUSERS >─ TBROLS
```

---

## Librerías principales y su función

| Librería | Uso |
|----------|-----|
| `spring-boot-starter-web` | Servidor HTTP, controladores REST y `RestTemplate` hacia la API central |
| `spring-boot-starter-data-jpa` | ORM (Hibernate) y repositorios con paginación |
| `ojdbc11` | Driver JDBC de Oracle |
| `spring-boot-starter-security` | Protección de rutas por rol |
| `jjwt-api` / `jjwt-impl` / `jjwt-jackson` | Generación y validación de tokens JWT |
| `argon2-jvm` | Hash seguro de contraseñas |
| `spring-boot-starter-validation` | Validación de DTOs |
| `spring-boot-starter-mail` | Envío del código de recuperación de contraseña |
| `cloudinary-http5` / `cloudinary-taglib` | Subida de imágenes |
| `lombok` | Getters, setters y constructores generados |

---

## Convenciones de nomenclatura

| Convención | Dónde se usa |
|---|---|
| `PascalCase` + sufijo de capa | `ResourceController`, `ResourceService`, `ResourceRepository`, `ResourceEntity`, `ResourceDTO`. |
| `camelCase` | Métodos, variables y rutas de endpoints (`/getAllResources`, `/insertConsumption`). |
| Prefijo `api` + dominio | Prefijo de cada controlador (`/apiResource`, `/apiMeasure`, `/apiConsumption`). |
| Verbo CRUD al inicio | `getAll…`, `get…ById/{id}`, `get…ByName/{name}`, `insert…`, `update…/{id}`, `delete…/{id}`. |
| Sufijo `List` | Endpoints que devuelven la lista completa sin paginar (`/getAllResourcesList`); sin sufijo son paginados. |
| `UPPER_CASE` con prefijo `TB` | Tablas de Oracle (`TBRESOURCES`, `TBCONSUMPTIONS`) y columnas (`IDCONSUMPTION`, `CONSUMPTIONDATE`). |

---

## Endpoints de la API

La sesión viaja en la cookie **`authToken`** (JWT). El `status` del usuario dentro del token define su rol:

| Rol | Permisos |
|---|---|
| `ROLE_1` | Lectura y escritura (crear, editar y eliminar) |
| `ROLE_0` | Solo lectura (consultas, reportes y mapa) |

### 1. Autenticación (`/apiAuth`)
- `POST /login`, `POST /shortLogin` — inicio de sesión (sesión de 24 h o de 1 h) _(públicos)_
- `POST /loginCompany` — login de la empresa, reenviado a la API central
- `GET /me` — usuario autenticado; `GET /company` — datos de la empresa
- `POST /logout`, `POST /forgotPassword`, `POST /verifyCode` _(públicos)_

### 2. Gestión

| Prefijo de ruta | Descripción |
|---|---|
| `/apiResource` | Recursos y purezas; `getResourceConsumptionsCO2/{id}`, `getAllResourcesConsumptionsCO2`, `getAllResourceConsumptionsCO2Total` |
| `/apiMeasure` | Medidas, unidades de medida, unidades de conversión y `convertMeasureUnit` |
| `/apiConsumption` | CRUD de consumos, `getConsumptionsByFilters`, `getConsumptionCO2/{id}` |
| `/apiUser` | Usuarios y roles, `updateDisabled/{id}`, `confirmPassword`, `updateUserPassword/{id}` |
| `/apiChannel` | `getChannel`, `updateChannel`, `getChannelPostApproved`, `getChannelPostNoApproved`, `approvePost/{id}`, `deletePost/{id}` (reenviados a la API central) |
| `/api/image` | `upload`, `upload-to-folder` (Cloudinary) |

---

## Base de datos

El repositorio incluye el script completo de la base de datos en [`Database/KarbonZeroInstitucional.sql`](Database/KarbonZeroInstitucional.sql). Se publica **con fines educativos y de referencia**: permite estudiar el modelo de datos del proyecto y levantar el ecosistema completo en un entorno local, sin depender de la infraestructura en la nube que se utilizó durante su desarrollo.

El script contiene la estructura completa y un conjunto de **datos de demostración**. Los usuarios de ejemplo usan correos ficticios (`@karbonzero.local`) y la misma contraseña de prueba (`KarbonZero2026!`); no incluye datos personales reales ni credenciales de ningún servidor.

| Contenido | Detalle |
|---|---|
| Tablas | 8 tablas (`TB*`) con llaves primarias, foráneas y restricciones `UNIQUE` y `CHECK` |
| Funciones y procedimientos | 7 funciones auxiliares (`get_id…_from_name`, `get_idresourcepurity_from_idresource_purity`, …) y el procedimiento `inverse_conversion_unit`, que genera la conversión inversa de una unidad |
| Datos de demostración | Medidas, unidades y conversiones; recursos (gasolina, diésel, agua, electricidad, papel, refrigerante) con sus purezas; 4 roles, 7 usuarios y consumos de ejemplo |

Para crearla en Oracle Database XE (21c o superior):

1. Como administrador (`SYSTEM` o `/ as sysdba`), crea el usuario de la API en la base conectable `XEPDB1`:

   ```sql
   ALTER SESSION SET CONTAINER = XEPDB1;
   CREATE USER KARBONZERO_INST IDENTIFIED BY "tu_contraseña" DEFAULT TABLESPACE USERS QUOTA UNLIMITED ON USERS;
   GRANT CREATE SESSION, CREATE TABLE, CREATE PROCEDURE, CREATE VIEW, CREATE SEQUENCE TO KARBONZERO_INST;
   ```

2. Conectado con ese usuario, ejecuta el script completo (en SQL Developer: abrir el archivo y **F5 — Ejecutar script**):

   ```bash
   sqlplus KARBONZERO_INST/"tu_contraseña"@//localhost:1521/XEPDB1 @Database/KarbonZeroInstitucional.sql
   ```

3. Coloca ese usuario y contraseña en `DB_USER` / `DB_PASSWORD` del `.env` (ver [Variables de entorno](#variables-de-entorno)).

> **Importante:** esta API depende de la API central para el login de empresa y el canal. La base de la API central ([`KarbonZero_BackendMobile/Database/KarbonZero.sql`](https://github.com/Tato0408/KarbonZero_BackendMobile/blob/main/Database/KarbonZero.sql)) ya trae registrada la empresa **Ricaldone** con `BACKENDDOMAIN = http://localhost:8081`; sus datos son los que usa el `.env.example`.

---

## Variables de entorno

Ninguna credencial está escrita en el código: `application.properties` y las clases de configuración solo hacen referencia a variables. En local se leen de un archivo **`.env`** en la raíz del proyecto (cargado con `spring.config.import`, ya está en `.gitignore`); en Heroku se leen de las **Config Vars**, que tienen prioridad sobre el `.env`.

| Variable | Obligatoria | Descripción |
|----------|:---:|-------------|
| `DB_URL` / `DB_USER` / `DB_PASSWORD` / `DB_DRIVER` | Sí | Conexión a la base de datos Oracle |
| `SERVER_PORT` | No _(default `8081`)_ | Puerto local de la API (la central usa 8080) |
| `COOKIE_DOMAIN` | No _(vacío en local)_ | Dominio de la cookie de sesión; en producción, el dominio público de esta API |
| `MAIL_USERNAME` / `MAIL_PASSWORD` | Sí | Cuenta de Gmail y contraseña de aplicación para enviar correos |
| `SECRET_KEY_JWT` / `SECURITY_JWT_ISSUER` / `JWT_EXPIRATION` | Sí | Firma, emisor y duración (ms) del JWT |
| `EXTERNAL_API_KEY` | Sí | API Key de la empresa en la API central |
| `EXTERNAL_DOMAIN` | Sí | URL de esta API registrada como `BACKENDDOMAIN` de la empresa; se envía en `X-Company-Domain` |
| `EXTERNAL_ADMIN_DOMAIN` | Sí | URL base de la API central de KarbonZero |
| `CLOUDINARY_CLOUD_NAME` / `CLOUDINARY_API_KEY` / `CLOUDINARY_API_SECRET` | Para imágenes | Credenciales de Cloudinary |

Plantilla lista para copiar (`cp .env.example .env`), con los valores de la empresa de demostración:

```env
# ── Base de datos (Oracle local) ─────────────────────────────────
DB_URL=jdbc:oracle:thin:@//localhost:1521/XEPDB1
DB_USER=KARBONZERO_INST
DB_PASSWORD=tu_contraseña
DB_DRIVER=oracle.jdbc.OracleDriver

# ── JWT ──────────────────────────────────────────────────────────
SECRET_KEY_JWT=una_clave_secreta_larga_y_dificil_de_adivinar_de_al_menos_32_caracteres
SECURITY_JWT_ISSUER=karbonZeroInstitutional
JWT_EXPIRATION=86400000

# ── Correo saliente (Gmail con contraseña de aplicación) ─────────
MAIL_USERNAME=tu_correo@gmail.com
MAIL_PASSWORD=tu_contraseña_de_aplicacion

# ── API central de KarbonZero (empresa demo "Ricaldone") ─────────
EXTERNAL_API_KEY=fdc7241db6bf9adb2809b3dac525ba1c
EXTERNAL_DOMAIN=http://localhost:8081
EXTERNAL_ADMIN_DOMAIN=http://localhost:8080
```

> **Nota:** en Heroku, define las mismas variables en **Settings → Config Vars** (o con `heroku config:set`).

---

## Ejecución del Proyecto

Requiere la base de datos creada (ver [Base de datos](#base-de-datos)), el archivo `.env` configurado y la **API central corriendo** en `http://localhost:8080`.

```bash
# Windows
mvnw.cmd spring-boot:run

# Linux / Mac
./mvnw spring-boot:run      # http://localhost:8081
```

Generar el `.war` para producción:

```bash
./mvnw clean package        # genera target/karbonzero-0.0.1-SNAPSHOT.war
```

---

## Solución de problemas comunes

| Síntoma | Causa | Solución |
|---|---|---|
| `IO Error: The Network Adapter could not establish the connection` | Oracle XE no está corriendo | Inicia los servicios `OracleServiceXE` y `OracleOraDB21Home*TNSListener` (en `services.msc`) |
| `ORA-01017` (usuario/contraseña inválidos) | Datos de `DB_USER` / `DB_PASSWORD` incorrectos | Verifica el usuario creado en el paso 1 de [Base de datos](#base-de-datos) |
| `Could not resolve placeholder 'DB_URL'` al arrancar | No existe el `.env` o falta esa variable | Crea el `.env` a partir de `.env.example` (o define la Config Var en Heroku) |
| El login de empresa o el canal responden error | `EXTERNAL_API_KEY`, `EXTERNAL_DOMAIN` o `EXTERNAL_ADMIN_DOMAIN` incorrectos, o la API central está caída | Verifica los datos de la empresa en el panel de gestión y que la API central responda |
| `403 Forbidden` al crear/editar | El rol del usuario tiene `status` 0 (solo lectura) | Inicia sesión con un usuario cuyo rol tenga `status` 1 (en los datos demo, `freddy@karbonzero.local`) |
| Respuesta `Token no encontrado` | La cookie `authToken` no se envió | Usa `credentials: "include"` en el frontend y revisa los orígenes de `CorsConfig.java` |
| Error de CORS | El origen del frontend no está permitido | En local se acepta `http://localhost` en cualquier puerto; para otros orígenes agrégalos en `CorsConfig.java` |

---

Este proyecto es de uso académico. Todos los derechos reservados.
