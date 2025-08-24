package com.nttdata.fhuichic.entrypoint.restcontroller;

import com.nttdata.fhuichic.module.temperature.application.dto.Unity;
import com.nttdata.fhuichic.module.temperature.application.service.create.TemperatureCreator;
import com.nttdata.fhuichic.module.temperature.application.service.get.TemperatureGetter;
import com.nttdata.fhuichic.module.temperature.infrastructure.dto.TemperatureRequestDto;
import com.nttdata.fhuichic.module.temperature.infrastructure.exceptions.TemperatureExceptionHandler;
import com.nttdata.fhuichic.module.temperature.infrastructure.mapper.TemperatureInteractionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Component
@RequiredArgsConstructor
public class TemperatureHandler {

    private final TemperatureCreator temperatureCreator;
    private final TemperatureGetter temperatureGetter;
    private final TemperatureInteractionMapper temperatureInteractionMapper;
    private final TemperatureExceptionHandler temperatureExceptionHandler;

    public Mono<ServerResponse> postTemperature(ServerRequest request) {
        return request.bodyToMono(TemperatureRequestDto.class)
                .map(temperatureInteractionMapper::toEntity)
                .flatMap(temperatureCreator::create)
                .map(temperatureInteractionMapper::toDtoResponse)
                .flatMap(savedTemperature -> ServerResponse.status(HttpStatus.CREATED)
                        .bodyValue(savedTemperature))
                .onErrorResume(temperatureExceptionHandler::toServerResponse);
    }

    public Mono<ServerResponse> getTemperature(ServerRequest request)  {
        return Mono.fromCallable(() -> {
            String dateStr = request.pathVariable("date");
            Optional<String> hourMinStr = request.queryParam("hourMin");
            Optional<String> hourMaxStr = request.queryParam("hourMax");
            Optional<String> unityStr = request.queryParam("unity");

            Unity unity = Unity.valueOf(unityStr.orElse(Unity.C.toString()));

            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            sdf.setLenient(false);
            Date date = sdf.parse(dateStr);

            return new ParsedRequest(date, hourMinStr, hourMaxStr, unity);
        })
        .flatMap(parsed -> {
            if (parsed.hourMinStr.isPresent() && parsed.hourMaxStr.isPresent()) {
                int hourMin = Integer.parseInt(parsed.hourMinStr.get());
                int hourMax = Integer.parseInt(parsed.hourMaxStr.get());

                return temperatureGetter.getHourlyStats(parsed.date, hourMin, hourMax, parsed.unity)
                        .flatMap(dto -> ok()
                                .header("X-Unity-Header", dto.getUnity().name())
                                .bodyValue(temperatureInteractionMapper.hourlyStatsToHourlyResponseDto(dto)));
            }

            return temperatureGetter.getDailyStats(parsed.date, parsed.unity)
                    .flatMap(dto -> ok()
                            .header("X-Unity-Header", dto.getUnity().name())
                            .bodyValue(temperatureInteractionMapper.dailyStatsToDailyResponseDto(dto)));
        })
        .onErrorResume(temperatureExceptionHandler::toServerResponse);


    }

    static class ParsedRequest {
        Date date;
        Optional<String> hourMinStr;
        Optional<String> hourMaxStr;
        Unity unity;
        ParsedRequest(Date date, Optional<String> hourMinStr, Optional<String> hourMaxStr, Unity unity) {
            this.date = date;
            this.hourMinStr = hourMinStr;
            this.hourMaxStr = hourMaxStr;
            this.unity = unity;
        }
    }

}
