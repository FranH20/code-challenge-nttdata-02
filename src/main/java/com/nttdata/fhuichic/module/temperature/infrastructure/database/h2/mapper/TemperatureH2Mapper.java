package com.nttdata.fhuichic.module.temperature.infrastructure.database.h2.mapper;

import com.nttdata.fhuichic.module.temperature.domain.model.Temperature;
import com.nttdata.fhuichic.module.temperature.infrastructure.database.h2.entity.TemperatureH2Entity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TemperatureH2Mapper {

    TemperatureH2Entity temperatureToEntity(Temperature temperature);

    Temperature entityToTemperature(TemperatureH2Entity entity);

}
