package com.nttdata.fhuichic.module.temperature.domain.port;

import com.nttdata.fhuichic.module.temperature.domain.model.Temperature;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Date;

public interface TemperatureService {

    Mono<Temperature> save(Temperature temperature);

    Flux<Temperature> findByDate(Date date);

    Flux<Temperature> findByDateAndHours(Date date, int hourMin, int hourMax);

}
