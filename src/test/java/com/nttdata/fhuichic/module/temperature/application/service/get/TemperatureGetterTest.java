package com.nttdata.fhuichic.module.temperature.application.service.get;

import com.nttdata.fhuichic.module.temperature.application.dto.DailyStatsDto;
import com.nttdata.fhuichic.module.temperature.application.dto.HourlyStatsDto;
import com.nttdata.fhuichic.module.temperature.application.dto.Unity;
import com.nttdata.fhuichic.module.temperature.application.mapper.StatsMapper;
import com.nttdata.fhuichic.module.temperature.domain.model.Temperature;
import com.nttdata.fhuichic.module.temperature.domain.port.TemperatureService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TemperatureGetterTest {

    @InjectMocks
    private TemperatureGetter temperatureGetter;

    @Mock
    private TemperatureService temperatureService;

    @Mock
    private StatsMapper statsMapper;

    @Test
    @DisplayName("Debe devolver las estadísticas diarias para un día con datos")
    void getDailyStats_ShouldReturnStatsWhenDataExists() {
        Date date = new Date();
        Unity unity = Unity.C;
        List<Temperature> temperatures = List.of(
                Temperature.builder().id(1L).value(10.0).dateTime(LocalDateTime.now()).build(),
                Temperature.builder().id(2L).value(20.0).dateTime(LocalDateTime.now()).build()
        );

        DailyStatsDto expectedDto = DailyStatsDto.builder()
                .date(date.toString())
                .min(10.0)
                .max(20.0)
                .average(15.0)
                .unity(unity).build();

        when(temperatureService.findByDate(date)).thenReturn(Flux.fromIterable(temperatures));
        when(statsMapper.toDailyStatsDto(any(Date.class), anyDouble(), anyDouble(), anyDouble(), any(Unity.class)))
                .thenReturn(expectedDto);

        StepVerifier.create(temperatureGetter.getDailyStats(date, unity))
                .expectNext(expectedDto)
                .verifyComplete();

        Mockito.verify(temperatureService).findByDate(date);
        Mockito.verify(statsMapper, times(1)).toDailyStatsDto(any(Date.class), anyDouble(), anyDouble(), anyDouble(), any(Unity.class));
    }

    @Test
    @DisplayName("Debe devolver estadísticas con NaN cuando no hay datos para el día")
    void getDailyStats_ShouldReturnDefaultWhenDataIsEmpty() {
        Date date = new Date();
        Unity unity = Unity.F;
        DailyStatsDto expectedDto = DailyStatsDto.builder()
                .date(date.toString())
                .min(Double.NaN)
                .max(Double.NaN)
                .average(Double.NaN)
                .unity(unity).build();

        when(temperatureService.findByDate(date)).thenReturn(Flux.empty());

        // <-- Se usa lenient() para evitar el TooManyActualInvocations
        lenient().when(statsMapper.toDailyStatsDto(any(Date.class), anyDouble(), anyDouble(), anyDouble(), any(Unity.class)))
                .thenReturn(expectedDto);

        StepVerifier.create(temperatureGetter.getDailyStats(date, unity))
                .expectNext(expectedDto)
                .verifyComplete();

        Mockito.verify(temperatureService).findByDate(date);
        // Verificamos que se llamó una sola vez en el flujo reactivo
        Mockito.verify(statsMapper, times(1)).toDailyStatsDto(any(Date.class), anyDouble(), anyDouble(), anyDouble(), any(Unity.class));
    }

    @Test
    @DisplayName("Debe devolver las estadísticas por hora para un rango con datos")
    void getHourlyStats_ShouldReturnStatsWhenDataExists() {
        Date date = new Date();
        int hourMin = 9;
        int hourMax = 12;
        Unity unity = Unity.F;
        List<Temperature> temperatures = List.of(
                Temperature.builder().id(1L).value(15.0).dateTime(LocalDateTime.now()).build(),
                Temperature.builder().id(2L).value(25.0).dateTime(LocalDateTime.now()).build()
        );
        HourlyStatsDto expectedDto = HourlyStatsDto.builder()
                .min(59.0)
                .max(77.0)
                .average(68.0)
                .unity(unity)
                .build();

        when(temperatureService.findByDateAndHours(date, hourMin, hourMax)).thenReturn(Flux.fromIterable(temperatures));

        lenient().when(statsMapper.toHourlyStatsDto(anyInt(), anyInt(), anyDouble(), anyDouble(), anyDouble(), any(Unity.class)))
                .thenReturn(expectedDto);

        StepVerifier.create(temperatureGetter.getHourlyStats(date, hourMin, hourMax, unity))
                .expectNext(expectedDto)
                .verifyComplete();

        Mockito.verify(temperatureService).findByDateAndHours(date, hourMin, hourMax);
        Mockito.verify(statsMapper).toHourlyStatsDto(anyInt(), anyInt(), anyDouble(), anyDouble(), anyDouble(), any(Unity.class));
    }

    @Test
    @DisplayName("Debe devolver estadísticas con NaN cuando no hay datos para el rango de horas")
    void getHourlyStats_ShouldReturnDefaultWhenDataIsEmpty() {
        Date date = new Date();
        int hourMin = 9;
        int hourMax = 12;
        Unity unity = Unity.C;
        HourlyStatsDto expectedDto = HourlyStatsDto.builder()
                .min(Double.NaN)
                .max(Double.NaN)
                .average(Double.NaN)
                .unity(unity).build();

        when(temperatureService.findByDateAndHours(date, hourMin, hourMax)).thenReturn(Flux.empty());

        lenient().when(statsMapper.toHourlyStatsDto(anyInt(), anyInt(), anyDouble(), anyDouble(), anyDouble(), any(Unity.class)))
                .thenReturn(expectedDto);

        StepVerifier.create(temperatureGetter.getHourlyStats(date, hourMin, hourMax, unity))
                .expectNext(expectedDto)
                .verifyComplete();

        Mockito.verify(temperatureService).findByDateAndHours(date, hourMin, hourMax);
        Mockito.verify(statsMapper, times(1)).toHourlyStatsDto(anyInt(), anyInt(), anyDouble(), anyDouble(), anyDouble(), any(Unity.class));
    }
}