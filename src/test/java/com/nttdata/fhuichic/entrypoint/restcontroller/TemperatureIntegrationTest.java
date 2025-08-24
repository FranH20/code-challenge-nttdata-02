package com.nttdata.fhuichic.entrypoint.restcontroller;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TemperatureIntegrationTest {

    @LocalServerPort
    private Integer port;

    @BeforeEach
    void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
    }

    @Test
    void testGetByDate() {
        given()
                .accept("application/json")
                .when()
                .get("/api/v1/temperature/10-08-2025")
                .then()
                .statusCode(200)
                .header("X-Unity-Header", equalTo("C"))
                .body("min", any(Number.class))
                .body("max", any(Number.class))
                .body("average", any(Number.class))
                .body("$", hasKey("date"));
    }

    @Test
    void testGetByDateFahrenheit() {
        given()
                .accept("application/json")
                .when()
                .get("/api/v1/temperature/10-08-2025?unity=F")
                .then()
                .statusCode(200)
                .header("X-Unity-Header", equalTo("F"))
                .body("min", any(Number.class))
                .body("max", any(Number.class))
                .body("average", any(Number.class))
                .body("$", hasKey("date"));
    }

    @Test
    void testGetByRangeHours() {
        given()
                .accept("application/json")
                .when()
                .get("/api/v1/temperature/10-08-2025?hourMin=10&hourMax=22")
                .then()
                .statusCode(200)
                .header("X-Unity-Header", equalTo("C"))
                .body("min", any(Number.class))
                .body("max", any(Number.class))
                .body("average", any(Number.class))
                .body("$", hasKey("time"));
    }

    @Test
    void testGetByRangeHoursFahrenheit() {
        given()
                .accept("application/json")
                .when()
                .get("/api/v1/temperature/10-08-2025?hourMin=10&hourMax=22&unity=F")
                .then()
                .statusCode(200)
                .header("X-Unity-Header", equalTo("F"))
                .body("min", any(Number.class))
                .body("max", any(Number.class))
                .body("average", any(Number.class))
                .body("$", hasKey("time"));
    }

    @Test
    void testPostTemperature() {
        String json = "{\n" +
                "  \"valor\": 20,\n" +
                "  \"fecha-hora\": \"10/08/2025 18:36:00\"\n" +
                "}";
        given()
                .contentType("application/json")
                .body(json)
                .when()
                .post("/api/v1/temperature")
                .then()
                .statusCode(201)
                .body("id", notNullValue())
                .body("valor", any(Number.class))
                .body("fecha-hora", equalTo("10/08/2025 18:36:00"));
    }

    @Test
    void whenInvalidDateFormat_thenReturnsBadRequest() {
        given()
                .accept("application/json")
                .when()
                .get("/api/v1/temperature/1998-01-20")
                .then()
                .statusCode(400)
                .body("message", equalTo("Formato de fecha o hora inválido"))
                .body("status", equalTo(HttpStatus.BAD_REQUEST.value()));
    }

    @Test
    void whenInvalidUnity_thenReturnsBadRequest() {
        given()
                .accept("application/json")
                .when()
                .get("/api/v1/temperature/10-08-2025?unity=XYZ") // fuerza IllegalArgumentException
                .then()
                .statusCode(400)
                .body("message", equalTo("Argumento de la petición inválido"))
                .body("status", equalTo(HttpStatus.BAD_REQUEST.value()));
    }

    @Test
    void whenUnexpectedError_thenReturnsInternalServerError() {
        given()
                .contentType("application/json")
                .body("{ \"valor\": 20 }")
                .when()
                .post("/api/v1/temperature")
                .then()
                .statusCode(500)
                .body("message", equalTo("Error inesperado en el servidor"))
                .body("status", equalTo(HttpStatus.INTERNAL_SERVER_ERROR.value()));

    }


}