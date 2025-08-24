package com.nttdata.fhuichic.module.temperature.application.service.get;

import com.nttdata.fhuichic.module.temperature.application.dto.DailyStatsDto;
import com.nttdata.fhuichic.module.temperature.application.dto.HourlyStatsDto;
import com.nttdata.fhuichic.module.temperature.application.dto.Stats;
import com.nttdata.fhuichic.module.temperature.application.dto.Unity;
import com.nttdata.fhuichic.module.temperature.application.mapper.StatsMapper;
import com.nttdata.fhuichic.module.temperature.domain.model.Temperature;
import com.nttdata.fhuichic.module.temperature.domain.port.TemperatureService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Date;
import java.util.List;
import java.util.OptionalDouble;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class TemperatureGetter {

    private final TemperatureService temperatureService;
    private final StatsMapper statsMapper;

    public Mono<DailyStatsDto> getDailyStats(Date date, Unity unity) {
        log.info("Get daily temperature stats for date: {}", date);

        return temperatureService.findByDate(date)
                .collectList()
                .map(temperatures -> getStatsForDailyDto(date, unity, temperatures))
                .switchIfEmpty(Mono.defer(() -> Mono.just(statsMapper.toDailyStatsDto(date, Double.NaN, Double.NaN, Double.NaN, unity))));
    }

    public Mono<HourlyStatsDto> getHourlyStats(Date date, int hourMin, int hourMax, Unity unity) {
        log.info("Get hourly temperature stats for date: {}", date);

        return temperatureService.findByDateAndHours(date, hourMin, hourMax)
                .collectList()
                .map(temperatures -> getStatsForHourlyDto(hourMin, hourMax, unity, temperatures))
                .switchIfEmpty(Mono.defer(() -> Mono.just(statsMapper.toHourlyStatsDto(hourMin, hourMax, Double.NaN, Double.NaN, Double.NaN, unity))));
    }

    private DailyStatsDto getStatsForDailyDto(Date date, Unity unity, List<Temperature> temperatures) {
        Stats stats = calculateAndConvertStats(temperatures, unity);
        return statsMapper.toDailyStatsDto(
                date,
                stats.getMin(),
                stats.getMax(),
                stats.getAverage(),
                unity
        );
    }

    private HourlyStatsDto getStatsForHourlyDto(int hourMin, int hourMax, Unity unity, List<Temperature> temperatures) {
        Stats stats = calculateAndConvertStats(temperatures, unity);
        return statsMapper.toHourlyStatsDto(
                hourMin,
                hourMax,
                stats.getMin(),
                stats.getMax(),
                stats.getAverage(),
                unity
        );
    }

    private Stats calculateAndConvertStats(List<Temperature> temperatures, Unity unity) {
        List<Double> values = temperatures.stream()
                .map(Temperature::getValue)
                .collect(Collectors.toList());

        OptionalDouble min = values.stream().mapToDouble(Double::doubleValue).min();
        OptionalDouble max = values.stream().mapToDouble(Double::doubleValue).max();
        OptionalDouble avg = values.stream().mapToDouble(Double::doubleValue).average();

        double finalMin = min.orElse(Double.NaN);
        double finalMax = max.orElse(Double.NaN);
        double finalAvg = avg.orElse(Double.NaN);

        if (Unity.F.equals(unity)) {
            finalMin = celsiusToFahrenheit(finalMin);
            finalMax = celsiusToFahrenheit(finalMax);
            finalAvg = celsiusToFahrenheit(finalAvg);
        }

        return new Stats(finalMin, finalMax, finalAvg);
    }

    private double celsiusToFahrenheit(double celsius) {
        if (Double.isNaN(celsius)) {
            return Double.NaN;
        }
        return (celsius * 9 / 5) + 32;
    }

}
