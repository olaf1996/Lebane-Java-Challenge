# API Departamentos en Venta

API REST para gestión de departamentos en venta. Challenge Backend Java (Spring Boot) — Lebane.

- **Stack:** Java 21, Spring Boot 3.4.0, Spring Data JPA, PostgreSQL, Bean Validation, MapStruct, Springdoc OpenAPI (Swagger).
- **Endpoints:** GET (listado con filtros), POST (crear), PUT (actualizar por id).

---

## Requisitos

- **Java 21**
- **Maven 3.9+**
- **PostgreSQL** (para ejecutar la API en local; en tests se usa H2 en memoria)
- **Docker y Docker Compose** (opcional, para ejecutar todo en contenedores)

---

## Instalación

Clonar el repositorio y entrar en la carpeta del proyecto:

```bash
cd Lebane-Java-Challenge
```

No hay pasos de instalación adicionales; las dependencias se resuelven con Maven.

---

## Ejecución

### Con Maven (app en local)

1. Tener PostgreSQL corriendo en `localhost:5432` con base `departamentos`, usuario `postgres`, contraseña `postgres` (o ajustar `application.yml`).

2. Opción: levantar solo la base con Docker:
   ```bash
   docker-compose up -d postgres
   ```

3. Arrancar la aplicación:
   ```bash
   mvn spring-boot:run
   ```

La API queda en **http://localhost:8080** (o en el puerto definido por `SERVER_PORT`).

### Con Docker (app + base de datos)

Levantar API y PostgreSQL con un solo comando:

```bash
docker-compose up -d
```

- **API:** http://localhost:8080  
- **PostgreSQL:** puerto 5432 (interno al compose)

```bash
docker-compose down
```

---

## Tests

```bash
mvn test
```

- **Tests de integración:** `@SpringBootTest` con perfil `test` y **H2 en memoria** (no requieren Docker). Cubren POST, GET y PUT.
- **Tests unitarios:** servicio (Mockito) y validaciones Bean Validation sobre `DepartamentoRequest`.

### ¿Por qué H2 en los tests?

Los tests de integración usan **H2 en memoria** (perfil `test`) en lugar de PostgreSQL con Testcontainers por dos razones:

1. **Portabilidad:** `mvn test` funciona en cualquier máquina (local, CI) sin depender de que Docker esté instalado o que el daemon sea accesible. En entornos como Windows, el daemon de Docker suele exponerse por un socket (`npipe`) que a veces no es usable desde el JVM que lanza Maven, lo que hace fallar Testcontainers.
2. **Misma capa de persistencia:** La API usa JPA sin SQL específico de PostgreSQL (sin arrays nativos, `jsonb`, etc.), así que el comportamiento con H2 es equivalente para estos tests: se valida el flujo completo Controller → Service → Repository → BD.

En producción y con Docker la aplicación sigue usando PostgreSQL; la decisión de H2 en tests es solo para que la suite sea fiable y ejecutable en cualquier entorno.

---

## Swagger / OpenAPI

Con la aplicación en marcha:

- **Swagger UI:** http://localhost:8080/swagger-ui.html  
- **OpenAPI JSON:** http://localhost:8080/v3/api-docs  

En la UI se pueden probar los endpoints y ver los códigos de respuesta documentados (200, 202, 400, 404).

---

## Contrato de la API

| Método | Ruta                      | Descripción                                           | Códigos principales                    |
|--------|----------------------------|-------------------------------------------------------|----------------------------------------|
| GET    | `/api/departamentos`       | Listar con filtros opc. `disponible`, `precioMin`, `precioMax` | 200 OK                                 |
| POST   | `/api/departamentos`       | Crear (body JSON, Bean Validation)                    | 202 Accepted, 400 Bad Request          |
| PUT    | `/api/departamentos/{id}`  | Actualizar por id                                     | 200 OK, 400 Bad Request, 404 Not Found |

Modelo de recurso: `id`, `titulo`, `descripcion`, `precio`, `moneda` (USD/ARS), `metrosCuadrados`, `direccion`, `disponible`, `imagenes` (lista de URLs opcional).

### Ejemplo de body (POST y PUT)

Campos obligatorios: `titulo`, `precio`, `moneda`, `metrosCuadrados`. El resto es opcional.

```json
{
  "titulo": "Depto 2 ambientes Centro",
  "descripcion": "Luminoso, cocina integrada.",
  "precio": 85000.50,
  "moneda": "USD",
  "metrosCuadrados": 45.5,
  "direccion": "Av. Corrientes 1234, CABA",
  "disponible": true,
  "imagenes": ["https://ejemplo.com/foto1.jpg"]
}
```

También podés probar los endpoints desde **Swagger UI** (ver sección anterior).

---

## Decisiones de diseño

| Caso                                                                 | Decisión                          | Código HTTP   |
|----------------------------------------------------------------------|-----------------------------------|---------------|
| Id inexistente en PUT                                                | Recurso no encontrado             | 404 Not Found |
| Id con formato inválido en PUT (ej. no numérico)                     | Petición mal formada              | 400 Bad Request |
| Body inválido en POST/PUT (campos faltantes, tipos erróneos, etc.)   | Validación fallida                | 400 Bad Request + cuerpo unificado |
| GET con filtros sin resultados                                       | Lista vacía `[]`                  | 200 OK        |

**Cuerpo unificado de error:** Todas las respuestas de error usan el mismo formato: `timestamp`, `status`, `error`, `message`, `path` y, en validación, `errors` con detalle por campo.

**Arquitectura:** Capas Controller → Service → Repository (JPA + JpaSpecificationExecutor). DTOs request/response con MapStruct. Excepciones propias (`DepartamentoNotFoundException`, `BadRequestException`) y `@RestControllerAdvice` para el cuerpo de error.

**Tests:** Integración con H2 en memoria (perfil `test`) para que `mvn test` funcione sin Docker en cualquier entorno.
