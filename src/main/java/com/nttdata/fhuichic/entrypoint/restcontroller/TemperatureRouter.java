package com.nttdata.fhuichic.entrypoint.restcontroller;

import com.nttdata.fhuichic.module.temperature.infrastructure.dto.TemperatureRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

@Configuration
public class TemperatureRouter {

    @Bean
    @RouterOperations({
            @RouterOperation(
                    path = "/api/v1/temperature",
                    produces = { MediaType.APPLICATION_JSON_VALUE },
                    method = RequestMethod.POST,
                    beanClass = TemperatureHandler.class,
                    beanMethod = "postTemperature",
                    operation = @Operation(
                            operationId = "createTemperature",
                            summary = "Registrar nueva temperatura",
                            requestBody = @RequestBody(
                                    required = true,
                                    description = "DTO con la información de la temperatura",
                                    content = @Content(schema = @Schema(implementation = TemperatureRequestDto.class))
                            ),
                            responses = {
                                    @ApiResponse(responseCode = "201", description = "Temperatura registrada correctamente"),
                                    @ApiResponse(responseCode = "400", description = "Error en el formato de la petición"),
                                    @ApiResponse(responseCode = "500", description = "Error inesperado en el servidor")
                            }
                    )
            ),
            @RouterOperation(
                    path = "/api/v1/temperature/{date}",
                    produces = { MediaType.APPLICATION_JSON_VALUE },
                    method = RequestMethod.GET,
                    beanClass = TemperatureHandler.class,
                    beanMethod = "getTemperature",
                    operation = @Operation(
                            operationId = "getTemperature",
                            summary = "Consultar estadísticas de temperatura",
                            parameters = {
                                    @Parameter(in = ParameterIn.PATH, name = "date", required = true, description = "Fecha en formato dd-MM-yyyy"),
                                    @Parameter(in = ParameterIn.QUERY, name = "hourMin", required = false, description = "Hora mínima"),
                                    @Parameter(in = ParameterIn.QUERY, name = "hourMax", required = false, description = "Hora máxima"),
                                    @Parameter(in = ParameterIn.QUERY, name = "unity", required = false, description = "Unidad de temperatura (C, F, K)")
                            },
                            responses = {
                                    @ApiResponse(responseCode = "200", description = "Consulta exitosa"),
                                    @ApiResponse(responseCode = "400", description = "Parámetros inválidos"),
                                    @ApiResponse(responseCode = "500", description = "Error inesperado en el servidor")
                            }
                    )
            )
    })
    public RouterFunction<ServerResponse> route(final TemperatureHandler temperatureHandler) {
        return RouterFunctions.route()
                .POST("/api/v1/temperature", accept(APPLICATION_JSON), temperatureHandler::postTemperature)
                .GET("/api/v1/temperature/{date}", temperatureHandler::getTemperature)
                .build();
    }

}
