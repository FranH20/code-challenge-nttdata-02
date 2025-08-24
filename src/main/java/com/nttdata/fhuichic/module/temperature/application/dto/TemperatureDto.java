package com.nttdata.fhuichic.module.temperature.application.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class TemperatureDto {

    private Long id;
    private Double value;
    private String dateTime;

}
