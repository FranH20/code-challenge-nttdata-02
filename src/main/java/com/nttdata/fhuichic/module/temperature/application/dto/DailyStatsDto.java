package com.nttdata.fhuichic.module.temperature.application.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class DailyStatsDto {

    private String date;
    private Double min;
    private Double max;
    private Double average;
    private Unity unity;

}
