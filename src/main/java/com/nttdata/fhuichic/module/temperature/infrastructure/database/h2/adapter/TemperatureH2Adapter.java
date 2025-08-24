package com.nttdata.fhuichic.module.temperature.infrastructure.database.h2.adapter;

import com.nttdata.fhuichic.module.temperature.domain.model.Temperature;
import com.nttdata.fhuichic.module.temperature.domain.port.TemperatureService;
import com.nttdata.fhuichic.module.temperature.infrastructure.database.h2.mapper.TemperatureH2Mapper;
import com.nttdata.fhuichic.module.temperature.infrastructure.database.h2.repository.TemperatureH2Repository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.*;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class TemperatureH2Adapter implements TemperatureService {

    private final TemperatureH2Repository temperatureRepository;
    private final TemperatureH2Mapper temperatureMapper;


    @Override
    public Mono<Temperature> save(Temperature temperature) {
        return temperatureRepository.save(temperatureMapper.temperatureToEntity(temperature))
                .map(temperatureMapper::entityToTemperature);
    }

    @Override
    public Flux<Temperature> findByDate(Date date) {
        LocalDate localDate = Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDateTime startOfDay = localDate.atStartOfDay();
        LocalDateTime endOfDay = localDate.atTime(LocalTime.MAX);

        return temperatureRepository.findByDateTimeBetween(startOfDay, endOfDay)
                .map(temperatureMapper::entityToTemperature);
    }

    @Override
    public Flux<Temperature> findByDateAndHours(Date date, int hourMin, int hourMax) {
        LocalDate localDate = Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDateTime startOfHour = localDate.atTime(LocalTime.of(hourMin, 0));
        LocalDateTime endOfHour = localDate.atTime(LocalTime.of(hourMax, 59, 59));

        return temperatureRepository.findByDateTimeBetween(startOfHour, endOfHour)
                .map(temperatureMapper::entityToTemperature);
    }

}
