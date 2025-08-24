package com.nttdata.fhuichic.module.temperature.application.mapper;

import com.nttdata.fhuichic.module.temperature.application.dto.TemperatureDto;
import com.nttdata.fhuichic.module.temperature.domain.model.Temperature;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TemperatureMapper {

    TemperatureDto toDto(Temperature temperature);

    Temperature toEntity(TemperatureDto temperatureDto);


}