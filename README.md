# Gestión de pedidos 

## Descripción

Es una aplicación desarrollada con **Spring Boot** para administrar la información relacionada con productos, clientes y pedidos. El proyecto está organizado siguiendo una estructura por capas, facilitando el mantenimiento y la escalabilidad del código.

## Tecnologías utilizadas

* Java
* Spring Boot
* Maven

## Endpoint Pedidos

| Método | Endpoint                         | Descripción                                                           |
|--------|----------------------------------|-----------------------------------------------------------------------|
| POST   | `/pedidos/crear`                 | Crear un pedido.                                                      |
| PUT    | `/pedidos/{id}/confirmar`        | Cambia el estado de un pedido pendiente a confirmado .                |
| PUT    | `/pedidos/{id}/cancelar`         | Cambia el estado de un pedido (pendiente,confirmado) a cancelado      |
| PUT    | `/pedidos/{id}/despachar`        | Cambia el estado de un pedido confirmado a despachado.                |
| GET    | `/pedidos`                       | Obtiene la lista de los pedidos.                                      | 
| GET    | `/pedidos?id=1`                  | Obtiene un pedido por la busqueda de su id.                           | 
| GET    | `/pedidos/estado/{estado}`       | Obtiene la lista de los pedidos filtrados por el estado.              |
| GET    | `/pedidos/prioridad/{prioridad}` | Obtiene la lista de los pedidos filtrados por la prioridad.           |
| GET    | `/pedidos/resumen`               | Obtiene una lista que muestra el resumen de los pedidos               |
| GET    | `/pedidos/riesgo`                | Obtiene la lista de los pedidos que no tienen stock en el inventario. |
| GET    | `/pedidos/siguiente`             | Obtiene el pedido que se atendera primero.                            |

## Endpoint Productos
| Método | Endpoint                  | Descripción                                   |
|--------|---------------------------|-----------------------------------------------|
| POST   | `/productos/crear`        | Crear un producto.                            |
| GET    | `/productos`              | Obtiene la lista de los productos.            |
| GET    | `/productos?id=1`         | Obtiene un producto por la busqueda de su id. |
| DELETE | `/productos/{productoId}` | Elimina un producto existente.                |


## Estructura del proyecto

```text
.
├── mvnw
├── mvnw.cmd
├── pom.xml
└── src
    ├── main
    │   ├── java
    │   │   └── co/sgp
    │   │       ├── Controller
    │   │       ├── Models
    │   │       ├── Services
    │   │       ├── Utils
    │   │       └── SgpApplication.java
    │   └── resources
    │       └── application.properties
    └── test
```

## Descripción de las carpetas

### `Controller/`

Contiene los controladores REST de la aplicación. Los controladores reciben las solicitudes HTTP, procesan la información y devuelven la respuesta correspondiente.

* `PedidosController.java`: administra las operaciones relacionadas con los pedidos.
* `ProductoController.java`: administra las operaciones relacionadas con los productos.

### `Models/`

Contiene las clases que representan las entidades del sistema.

#### `Cliente/`

* `Cliente.java`: representa la información de un cliente.

#### `Producto/`

* `Producto.java`: representa la información de un producto.

#### `Pedido/`

* `Pedido.java`: representa un pedido realizado por un cliente.
* `Estado.java`: define los estados posibles de un pedido.
* `Prioridad.java`: define los niveles de prioridad de un pedido.

### `Services/`
* `PedidoService.java` : métodos de la lógica de negocio para el módulo pedidos.
* `ProductoService.java` : métodos de la lógica de negocio para el módulo productos.
### `Utils/`

Contiene clases auxiliares reutilizables.

* `Validador.java`: incluye métodos para validar datos utilizados por la aplicación.

### `resources/`

Contiene los archivos de configuración del proyecto.

* `application.properties`: configuración de Spring Boot.

### `test/`

Contiene las pruebas unitarias de la aplicación.

* `SgpApplicationTests.java`: pruebas básicas del proyecto.

## `Endpoints`

## productos



## Estructura JSON para un nuevo producto
```json
{
  "productoId": 1234,
  "nombreProducto": "Borrador Miga de pan",
  "stock": 100
}
```

## Estructura JSON para un nuevo pedido

```json
{
  "NIT": 12312413,
  "nombreCliente": "Jose Rodrigo",
  "prioridad": "URGENTE",
  "cantidad": 1,
  "productoId": 87654
}
```
