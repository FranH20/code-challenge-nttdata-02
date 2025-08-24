package com.nttdata.fhuichic.module.temperature.domain.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
public class Temperature {

    private Long id;
    private LocalDateTime dateTime;
    private double value;

}
