package com.nttdata.fhuichic.module.temperature.infrastructure.database.h2.repository;

import com.nttdata.fhuichic.module.temperature.infrastructure.database.h2.entity.TemperatureH2Entity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;

@Repository
public interface TemperatureH2Repository extends R2dbcRepository<TemperatureH2Entity, Long> {

    Flux<TemperatureH2Entity> findByDateTimeBetween(LocalDateTime start, LocalDateTime end);

}
