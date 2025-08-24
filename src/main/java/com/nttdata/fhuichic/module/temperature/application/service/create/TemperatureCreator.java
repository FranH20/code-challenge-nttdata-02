package com.nttdata.fhuichic.module.temperature.application.service.create;

import com.nttdata.fhuichic.module.temperature.application.dto.TemperatureDto;
import com.nttdata.fhuichic.module.temperature.application.mapper.TemperatureMapper;
import com.nttdata.fhuichic.module.temperature.domain.model.Temperature;
import com.nttdata.fhuichic.module.temperature.domain.port.TemperatureService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Log4j2
public class TemperatureCreator {

    private final TemperatureService temperatureService;
    private final TemperatureMapper temperatureMapper;

    public Mono<Temperature> create(final TemperatureDto temperature) {
        log.info("Creating temperature: {}", temperature);
        return temperatureService.save(temperatureMapper.toEntity(temperature));
    }

}
