# Tienda Reactiva

CRUD reactivo de productos construido con Scaffold Clean Architecture 4.6.2, Spring WebFlux y R2DBC PostgreSQL.

## Arquitectura

- `domain/model`: entidades y puertos de salida, sin dependencias de infraestructura.
- `domain/usecase`: reglas y orquestacion del caso de uso `ProductUseCase`.
- `infrastructure/driven-adapters/r2dbc-postgresql`: implementacion del puerto con PostgreSQL reactivo.
- `infrastructure/entry-points/reactive-web`: API HTTP WebFlux.
- `applications/app-service`: composicion de modulos y punto de entrada Spring Boot.
- `feature/rabbitmq-events`: consumer y producer reactivos con RabbitMQ.

Las dependencias apuntan hacia adentro: el dominio no conoce PostgreSQL ni HTTP; los adapters conocen los puertos del dominio.

## Requisitos

- Java 21 (el scaffold admite Java 17+).
- Docker Engine en Ubuntu WSL2, sin Docker Desktop.

## Ejecutar PostgreSQL y RabbitMQ

Desde Ubuntu WSL, en la carpeta del proyecto montada como `/mnt/c/Users/.../Documents/Pruebas`:

```bash
docker compose up -d postgres rabbitmq
docker compose ps
```

PostgreSQL queda en `localhost:15432` y RabbitMQ en `localhost:5672`; la consola de RabbitMQ queda en `http://localhost:15672` con `tienda/tienda`.

## Flujo RabbitMQ

En la rama `feature/rabbitmq-events` se implemento el flujo completo:

1. El consumer `async-event-handler` escucha `product.create`.
2. `EventsHandler` deserializa el CloudEvent a `Product` y llama `ProductUseCase`.
3. `ProductUseCase` persiste mediante el puerto `ProductRepository` en PostgreSQL.
4. Al guardar, el puerto `EventsGateway` publica `product.created` usando `async-event-bus`.

El comando y el evento tienen nombres diferentes para evitar que el servicio consuma su propio evento y genere un ciclo.

La conexión se configura con `spring.rabbitmq` y admite `RABBITMQ_HOST`, `RABBITMQ_PORT`, `RABBITMQ_USER`, `RABBITMQ_PASSWORD` y `RABBITMQ_VHOST`.

## Ejecutar la API

En PowerShell, seleccionando un JDK 21:

```powershell
$env:JAVA_HOME = 'C:\Program Files\Amazon Corretto\jdk21.0.12_9'
./gradlew.bat :app-service:bootRun
```

Endpoints: `POST /api/products`, `GET /api/products`, `GET /api/products/{id}`, `PUT /api/products/{id}` y `DELETE /api/products/{id}`.

Ejemplo:

```powershell
Invoke-RestMethod http://localhost:8080/api/products -Method Post -ContentType 'application/json' -Body '{"name":"Cafe","price":12500.00,"stock":10}'
```

## Scaffolding usado

```powershell
gradle ca --name=TiendaReactiva --package=co.com.tienda --type=reactive --java-version=21 --lombok=true --metrics=false --mutation=false
gradle gm --name=Product
gradle guc --name=Product
gradle gda --type=r2dbc
gradle gep --type=webflux --router=false
```
# Proyecto Base Implementando Clean Architecture

## Antes de Iniciar

Empezaremos por explicar los diferentes componentes del proyectos y partiremos de los componentes externos, continuando con los componentes core de negocio (dominio) y por último el inicio y configuración de la aplicación.

Lee el artículo [Clean Architecture — Aislando los detalles](https://medium.com/bancolombia-tech/clean-architecture-aislando-los-detalles-4f9530f35d7a)

# Arquitectura

![Clean Architecture](https://miro.medium.com/max/1400/1*ZdlHz8B0-qu9Y-QO3AXR_w.png)

## Domain

Es el módulo más interno de la arquitectura, pertenece a la capa del dominio y encapsula la lógica y reglas del negocio mediante modelos y entidades del dominio.

## Usecases

Este módulo gradle perteneciente a la capa del dominio, implementa los casos de uso del sistema, define lógica de aplicación y reacciona a las invocaciones desde el módulo de entry points, orquestando los flujos hacia el módulo de entities.

## Infrastructure

### Helpers

En el apartado de helpers tendremos utilidades generales para los Driven Adapters y Entry Points.

Estas utilidades no están arraigadas a objetos concretos, se realiza el uso de generics para modelar comportamientos
genéricos de los diferentes objetos de persistencia que puedan existir, este tipo de implementaciones se realizan
basadas en el patrón de diseño [Unit of Work y Repository](https://medium.com/@krzychukosobudzki/repository-design-pattern-bc490b256006)

Estas clases no puede existir solas y debe heredarse su compartimiento en los **Driven Adapters**

### Driven Adapters

Los driven adapter representan implementaciones externas a nuestro sistema, como lo son conexiones a servicios rest,
soap, bases de datos, lectura de archivos planos, y en concreto cualquier origen y fuente de datos con la que debamos
interactuar.

### Entry Points

Los entry points representan los puntos de entrada de la aplicación o el inicio de los flujos de negocio.

## Application

Este módulo es el más externo de la arquitectura, es el encargado de ensamblar los distintos módulos, resolver las dependencias y crear los beans de los casos de use (UseCases) de forma automática, inyectando en éstos instancias concretas de las dependencias declaradas. Además inicia la aplicación (es el único módulo del proyecto donde encontraremos la función “public static void main(String[] args)”.

**Los beans de los casos de uso se disponibilizan automaticamente gracias a un '@ComponentScan' ubicado en esta capa.**
