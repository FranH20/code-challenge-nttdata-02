package com.nttdata.fhuichic.module.temperature.infrastructure.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
public class TemperatureResponseDto {

    @JsonProperty("id")
    private int id;

    @JsonProperty("valor")
    private Double value;

    @NotNull
    @JsonProperty("fecha-hora")
    private String dateTime;

}
