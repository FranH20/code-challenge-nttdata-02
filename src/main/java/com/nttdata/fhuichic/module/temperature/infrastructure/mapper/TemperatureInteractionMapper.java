package com.nttdata.fhuichic.module.temperature.infrastructure.mapper;

import com.nttdata.fhuichic.module.temperature.application.dto.DailyStatsDto;
import com.nttdata.fhuichic.module.temperature.application.dto.HourlyStatsDto;
import com.nttdata.fhuichic.module.temperature.application.dto.TemperatureDto;
import com.nttdata.fhuichic.module.temperature.domain.model.Temperature;
import com.nttdata.fhuichic.module.temperature.infrastructure.dto.TemperatureDailyResponseDto;
import com.nttdata.fhuichic.module.temperature.infrastructure.dto.TemperatureHourlyResponseDto;
import com.nttdata.fhuichic.module.temperature.infrastructure.dto.TemperatureRequestDto;
import com.nttdata.fhuichic.module.temperature.infrastructure.dto.TemperatureResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TemperatureInteractionMapper {

    TemperatureDto toEntity(TemperatureRequestDto dto);

    TemperatureRequestDto toDto(Temperature temperature);

    @Mapping(target = "dateTime", source = "dateTime", dateFormat = "dd/MM/yyyy HH:mm:ss")
    TemperatureResponseDto toDtoResponse(Temperature temperature);

    TemperatureHourlyResponseDto hourlyStatsToHourlyResponseDto(HourlyStatsDto hourlyStatsDto);

    TemperatureDailyResponseDto dailyStatsToDailyResponseDto(DailyStatsDto dailyStatsDto);

}
