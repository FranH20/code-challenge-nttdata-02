package com.nttdata.fhuichic.module.temperature.infrastructure.database.h2.adapter;

import com.nttdata.fhuichic.module.temperature.domain.model.Temperature;
import com.nttdata.fhuichic.module.temperature.infrastructure.database.h2.entity.TemperatureH2Entity;
import com.nttdata.fhuichic.module.temperature.infrastructure.database.h2.mapper.TemperatureH2Mapper;
import com.nttdata.fhuichic.module.temperature.infrastructure.database.h2.repository.TemperatureH2Repository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;

@ExtendWith(MockitoExtension.class)
class TemperatureH2AdapterTest {

    @InjectMocks
    private TemperatureH2Adapter temperatureH2Adapter;

    @Mock
    private TemperatureH2Repository temperatureRepository;
    @Mock
    private TemperatureH2Mapper temperatureMapper;

    @Test
    @DisplayName("El adaptador debe guardar una nueva temperatura correctamente y mapearla de vuelta a la entidad de dominio")
    void save_ShouldReturnASavedTemperature() {
        Temperature temperature = Temperature.builder()
                .id(null)
                .value(0.0)
                .dateTime(LocalDateTime.now())
                .build();

        TemperatureH2Entity h2Entity = new TemperatureH2Entity(1L, 0.0, LocalDateTime.now());

        Mockito.when(temperatureMapper.temperatureToEntity(Mockito.any(Temperature.class)))
                .thenReturn(h2Entity);

        Mockito.when(temperatureRepository.save(Mockito.any(TemperatureH2Entity.class)))
                .thenReturn(Mono.just(h2Entity));

        Mockito.when(temperatureMapper.entityToTemperature(Mockito.any(TemperatureH2Entity.class)))
                .thenReturn(temperature);

        temperatureH2Adapter.save(temperature)
                .doOnNext(Assertions::assertNotNull)
                .subscribe();
    }

    @Test
    @DisplayName("Debe obtener todas las temperaturas para un día específico")
    void findByDate_ShouldReturnTemperaturesForGivenDay() {
        Date date = Date.from(Instant.now());
        LocalDateTime startOfDay = Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate().atStartOfDay();
        LocalDateTime endOfDay = Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate().atTime(LocalTime.MAX);

        TemperatureH2Entity entity1 = new TemperatureH2Entity(1L, 25.5, startOfDay.plusHours(10));
        TemperatureH2Entity entity2 = new TemperatureH2Entity(2L, 26.0, startOfDay.plusHours(14));
        Temperature temperature1 = Temperature.builder().id(1L).value(25.5).build();
        Temperature temperature2 = Temperature.builder().id(2L).value(26.0).build();

        Mockito.when(temperatureRepository.findByDateTimeBetween(startOfDay, endOfDay))
                .thenReturn(Flux.just(entity1, entity2));
        Mockito.when(temperatureMapper.entityToTemperature(entity1)).thenReturn(temperature1);
        Mockito.when(temperatureMapper.entityToTemperature(entity2)).thenReturn(temperature2);

        Flux<Temperature> result = temperatureH2Adapter.findByDate(date);

        StepVerifier.create(result)
                .expectNext(temperature1)
                .expectNext(temperature2)
                .verifyComplete();

        Mockito.verify(temperatureRepository).findByDateTimeBetween(startOfDay, endOfDay);
        Mockito.verify(temperatureMapper, Mockito.times(2)).entityToTemperature(Mockito.any(TemperatureH2Entity.class));
    }

    @Test
    @DisplayName("Debe obtener todas las temperaturas para un rango de horas específico")
    void findByDateAndHours_ShouldReturnTemperaturesForGivenHourRange() {
        Date date = Date.from(Instant.now());
        int hourMin = 9;
        int hourMax = 12;

        LocalDateTime startOfHour = Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate().atTime(LocalTime.of(hourMin, 0));
        LocalDateTime endOfHour = Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate().atTime(LocalTime.of(hourMax, 59, 59));

        TemperatureH2Entity entity = new TemperatureH2Entity(1L, 20.1, startOfHour.plusHours(1));
        Temperature temperature = Temperature.builder().id(1L).value(20.1).build();

        Mockito.when(temperatureRepository.findByDateTimeBetween(startOfHour, endOfHour))
                .thenReturn(Flux.just(entity));
        Mockito.when(temperatureMapper.entityToTemperature(entity)).thenReturn(temperature);

        Flux<Temperature> result = temperatureH2Adapter.findByDateAndHours(date, hourMin, hourMax);

        StepVerifier.create(result)
                .expectNext(temperature)
                .verifyComplete();

        Mockito.verify(temperatureRepository).findByDateTimeBetween(startOfHour, endOfHour);
        Mockito.verify(temperatureMapper).entityToTemperature(entity);
    }
}