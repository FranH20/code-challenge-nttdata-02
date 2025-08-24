package com.nttdata.fhuichic.module.temperature.application.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class HourlyStatsDto {

    private String time;
    private Double min;
    private Double max;
    private Double average;
    private Unity unity;

}
