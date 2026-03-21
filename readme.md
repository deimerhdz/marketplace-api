#  Marketplace de Genética Bovina

##  Descripción

Este proyecto es un **marketplace digital especializado en genética bovina**, orientado a conectar proveedores (ganaderías, centros genéticos) con clientes interesados en adquirir material genético (pajillas/straws) de alta calidad.

La plataforma permite explorar un catálogo estructurado de toros, razas y presentaciones, gestionar inventario disponible y realizar compras de manera sencilla. Está diseñada con un enfoque escalable para evolucionar hacia funcionalidades avanzadas como evaluaciones genéticas, analítica y recomendaciones.

---

##  Tecnologías Utilizadas

* **Spring Boot** → Framework principal para el backend
* **PostgreSQL** → Base de datos relacional
* **Flyway** → Control de versiones y migraciones de base de datos
* **Docker** → Contenerización del entorno de desarrollo

---

##  Arquitectura

El proyecto sigue una arquitectura basada en principios de **Clean Architecture / DDD (Domain-Driven Design)**, separando responsabilidades en capas bien definidas:

```
domain/
persistence/
web/
```

---

###  1. Capa de Dominio (`domain`)

Contiene la lógica central del negocio y es completamente independiente de frameworks.

**Estructura:**

* `dto/` → Objetos de transferencia de datos
* `enums/` → Enumeraciones del dominio (ej: tipos de ganado, estados)
* `exception/` → Excepciones de negocio
* `repository/` → Interfaces (contratos) de acceso a datos
* `service/` → Lógica de negocio

 Esta capa define **las reglas del sistema** y no depende de infraestructura.

---

### ️ 2. Capa de Persistencia (`persistence`)

Encargada de la interacción con la base de datos (PostgreSQL).

**Estructura:**

* `entity/` → Entidades JPA (modelo relacional)
* `repository/` → Implementaciones de acceso a datos (Spring Data / JPA)
* `mapper/` → Conversión entre entidades y modelos de dominio
* `crud/` → Repositorios base o utilidades de persistencia

 Esta capa depende del dominio, pero el dominio **no depende de ella**.

---

### 3. Capa Web (`web`)

Exposición de la API REST.

**Estructura:**

* `controller/` → Endpoints HTTP
* `exception/` → Manejo global de errores (handlers)

 Esta capa orquesta las solicitudes del cliente y delega la lógica al dominio.

---

##  Flujo de la Aplicación

```
Controller → Service (domain) → Repository (domain) → Persistence → Database
```

* El controlador recibe la solicitud
* El servicio aplica la lógica de negocio
* El repositorio define el contrato de acceso
* La capa de persistencia ejecuta la operación en la base de datos

---

##  Base de Datos

* PostgreSQL como motor principal
* Flyway para:

    * versionar el esquema
    * mantener consistencia entre entornos
    * ejecutar migraciones automáticamente

---

##  Docker

El proyecto utiliza Docker para levantar dependencias como PostgreSQL:

```bash
docker compose up -d
```

Esto permite:

* entorno reproducible
* fácil configuración local
* aislamiento de servicios

---

##  Objetivo del MVP

El sistema está enfocado en un MVP con las siguientes capacidades:

* Catálogo de toros y razas
* Gestión básica de inventario
* Carrito de compras
* Creación de órdenes
* Registro de pagos

---

##  Evolución futura

* Evaluaciones genéticas avanzadas
* Sistema de recomendaciones
* Analítica de producción
* Gestión avanzada de inventario

---

##  Filosofía del diseño

* Separación clara de responsabilidades
* Bajo acoplamiento entre capas
* Preparado para escalar a microservicios
* Orientado al dominio del negocio

---

##  Ejecución del proyecto

1. Levantar la base de datos:

```bash
docker compose up -d
```

2. Ejecutar la aplicación:

```bash
./mvnw spring-boot:run
```

---

##  Autor

Deimer Hernandez
