# code-challenge-nttdata-02
Se requiere un ms que capture la temperatura + la fecha-hora en que se tomó, a su vez que permita exponer las métricas de max,min, average de temperatura por rango de horas y en un día dado.

## Stack
- Java 11
- Spring Boot
- Maven
- Spring Webflux
- H2 Database
- JUnit 5
- Mockito
- Swagger

## Endpoints del microservicio
La aplicación expone los siguientes endpoints en `http://localhost:8080/api/v1`:

### `POST /temperature`
- **Descripción:** Permite capturar la temperatura junto con la fecha y hora en que se tomó.

### `GET /temperature/{date}?hourMin={hourMin}&hourMax={hourMax}%unity={unity}`
- **Descripción:** Permite obtener las métricas de temperatura para un día específico o un rango de horas dado, ademas de que se puede escoger por Fahrenheit o Celsius.

### `GET http://localhost:8080/webjars/swagger-ui/index.html`
- **Descripción:** Permite acceder a la documentación de la API generada por Swagger.