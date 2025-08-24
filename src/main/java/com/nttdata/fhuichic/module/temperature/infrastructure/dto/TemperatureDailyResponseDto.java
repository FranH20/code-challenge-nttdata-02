package com.nttdata.fhuichic.module.temperature.infrastructure.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TemperatureDailyResponseDto {

    @JsonProperty("date")
    private String date;

    @JsonProperty("min")
    private double min;

    @JsonProperty("max")
    private double max;

    @JsonProperty("average")
    private double average;

}
