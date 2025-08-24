package com.nttdata.fhuichic.module.temperature.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Stats {
    private final double min;
    private final double max;
    private final double average;
}
