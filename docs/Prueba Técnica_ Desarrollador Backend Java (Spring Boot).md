## Prueba Técnica: Desarrollador Backend Java (Spring Boot)

### Introducción

Implementar una API REST utilizando **Spring Boot 3.x** para la gestión de departamentos en venta, exponiendo endpoints GET, POST y PUT, ejecutándose dentro de contenedores Docker.

El ejercicio está diseñado para que el candidato muestre:

* Dominio de **Spring Framework**.  
* Buenas prácticas de diseño (SOLID, Clean Architecture o Hexagonal).  
* Capacidad de razonamiento y justificación técnica.

### El desafío

Una empresa inmobiliaria necesita un backend robusto para registrar y consultar departamentos en venta.

Modelo de datos (base)

`{`  
  `"id": "string | int",`  
  `"titulo": "string",`  
  `"descripcion": "string",`  
  `"precio": "float",`  
  `"moneda": "USD | ARS",`  
  `"metros_cuadrados": "float",`  
  `"direccion": "string",`  
  `"disponible": "boolean"`  
`}`

*El candidato puede extender el modelo justificando la decisión (ej. agregar una lista de URLs de imágenes, geolocalización, etc.).*

Endpoints requeridos

1. **Crear departamento**  
   1. `POST /api/departamentos`  
   2. Validación de datos de entrada (Bean Validation).  
   3. Devuelve el recurso creado.  
   4. **Status esperado:** `202 Accepted`.  
2. **Listar departamentos**  
   1. `GET /api/departamentos`  
   2. Devuelve todos los departamentos.  
   3. Debe incluir filtros opcionales (Query Params): `disponible`, `precioMin`, `precioMax`. (Deseable: usar JPA Specifications)  
   4. **Status esperado:** `200 OK`  
3. **Actualizar departamento**  
   1. `PUT /api/departamentos/{id}`  
   2. Permite actualizar los campos del recurso.  
   3. Si el departamento no existe, devolver `404 Not Found`.  
   4. **Status esperado:** `200 OK`

Requerimientos técnicos (Java Stack)

* **Framework:** Spring Boot 3.x (Java 17 o 21+).  
* **Persistencia:** Spring Data JPA con **PostgreSQL**.  
* **Validación:** Hibernate Validator (JSR 380).  
* **Mapeo:** Uso de DTOs para separar la capa de persistencia de la de API (puedes usar MapStruct o mapeo manual).  
* **Gestión de errores:** Manejo global de excepciones mediante `@RestControllerAdvice`.  
* **Documentación:** Swagger/OpenAPI (Springdoc-openapi).

Docker

La aplicación debe estar completamente dockerizada. Se requiere:

* **Dockerfile:** Optimizado (multistage build preferentemente).  
* **Docker Compose:** Para levantar la API y la base de datos PostgreSQL como servicios vinculados.  
* Puerto de la app configurable mediante variables de entorno.

Calidad y Testing (Obligatorio)

Para nosotros, el código no está terminado hasta que está testeado:

* **Tests Unitarios:** Cobertura de la lógica de negocio y validaciones (JUnit 5 \+ Mockito).  
* **Tests de Integración:** Testeo de los endpoints (puedes usar `@SpringBootTest` con `Testcontainers` para la DB).

Entrega

1. Crear un repositorio privado en GitHub.  
2. Incluir un archivo `README.md` con:  
   1. Instrucciones de instalación y ejecución.  
   2. Guía para ejecutar la suite de tests.  
   3. Breve explicación de las decisiones de arquitectura tomadas.  
3. Invitar como colaborador al usuario: **LebaneRecruiting**

Criterios de Evaluación

* **Arquitectura de componentes:** Separación clara de responsabilidades.  
* **Manejo de errores:** Respuestas consistentes y códigos de estado HTTP correctos.  
* **Clean Code:** Legibilidad, nombres significativos y consistencia.  
* **Seguridad básica:** (Opcional pero valorado) Cualquier medida de resiliencia o validación extra.

Fecha de entrega

La fecha de entrega es hasta las **23:59:59 del 4to día** desde la presentación del enunciado.

