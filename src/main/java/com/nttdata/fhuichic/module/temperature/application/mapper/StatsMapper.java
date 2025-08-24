package com.nttdata.fhuichic.module.temperature.application.mapper;

import com.nttdata.fhuichic.module.temperature.application.dto.DailyStatsDto;
import com.nttdata.fhuichic.module.temperature.application.dto.HourlyStatsDto;
import com.nttdata.fhuichic.module.temperature.application.dto.Unity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Mapper(componentModel = "spring")
public interface StatsMapper {
    @Mappings({
            @Mapping(target = "date", expression = "java(formatDate(date))")
    })
    DailyStatsDto toDailyStatsDto(Date date, double min, double max, double average, Unity unity);

    @Mappings({
            @Mapping(target = "time", expression = "java(formatHourlyTime(hourMin, hourMax))")
    })
    HourlyStatsDto toHourlyStatsDto(int hourMin, int hourMax, double min, double max, double average, Unity unity);

    default String formatDate(Date date) {
        return new SimpleDateFormat("yyyy-MM-dd").format(date);
    }

    default String formatHourlyTime(int hourMin, int hourMax) {
        LocalTime timeMin = LocalTime.of(hourMin, 0);
        LocalTime timeMax = LocalTime.of(hourMax, 0);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return String.format("%s - %s", timeMin.format(formatter), timeMax.format(formatter));
    }
}
