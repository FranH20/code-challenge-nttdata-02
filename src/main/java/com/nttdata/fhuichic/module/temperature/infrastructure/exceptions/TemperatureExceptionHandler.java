package com.nttdata.fhuichic.module.temperature.infrastructure.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.text.ParseException;
import java.time.Instant;
import java.util.Map;

@Component
@Slf4j
public class TemperatureExceptionHandler {

    private final Map<Class<? extends Throwable>, HttpStatus> statusMap = Map.of(
            ParseException.class, HttpStatus.BAD_REQUEST,
            IllegalArgumentException.class, HttpStatus.BAD_REQUEST
    );

    private final Map<Class<? extends Throwable>, String> messageMap = Map.of(
            ParseException.class, "Formato de fecha o hora inválido",
            IllegalArgumentException.class, "Argumento de la petición inválido"
    );

    public Mono<ServerResponse> toServerResponse(Throwable ex) {
        HttpStatus status = statusMap.getOrDefault(ex.getClass(), HttpStatus.INTERNAL_SERVER_ERROR);
        String message = messageMap.getOrDefault(ex.getClass(), "Error inesperado en el servidor");

        Map<String, Object> body = Map.of(
                "message", message,
                "status", status.value(),
                "timestamp", Instant.now().toString()
        );

        return ServerResponse.status(status).bodyValue(body);
    }

}
