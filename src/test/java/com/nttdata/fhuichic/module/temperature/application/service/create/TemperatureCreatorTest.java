package com.nttdata.fhuichic.module.temperature.application.service.create;

import com.nttdata.fhuichic.module.temperature.application.dto.TemperatureDto;
import com.nttdata.fhuichic.module.temperature.application.mapper.TemperatureMapper;
import com.nttdata.fhuichic.module.temperature.domain.model.Temperature;
import com.nttdata.fhuichic.module.temperature.domain.port.TemperatureService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TemperatureCreatorTest {

    @InjectMocks
    private TemperatureCreator temperatureCreator;

    @Mock
    private TemperatureService temperatureService;

    @Mock
    private TemperatureMapper temperatureMapper;

    @Test
    @DisplayName("El test debe crear y obtener una temperatura")
    void create_ShouldReturnACreateTemperature() {

        Temperature temperature = Temperature.builder().id(1L).value(50.0).dateTime(LocalDateTime.now()).build();

        when(temperatureMapper.toEntity(any(TemperatureDto.class)))
                .thenReturn(temperature);

        when(temperatureService.save(any(Temperature.class)))
                .thenReturn(Mono.just(temperature));

        temperatureCreator.create(TemperatureDto.builder().id(1L).value(50.0).build())
                .doOnNext(Assertions::assertNotNull)
                .subscribe();

    }

}