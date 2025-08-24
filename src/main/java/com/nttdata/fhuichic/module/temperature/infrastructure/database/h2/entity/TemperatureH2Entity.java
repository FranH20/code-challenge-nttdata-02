package com.nttdata.fhuichic.module.temperature.infrastructure.database.h2.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("temperatures")
public class TemperatureH2Entity {

    @Id
    private Long id;
    @Column(value = "temperature_value")
    private double value;
    @Column(value = "date_time")
    private LocalDateTime dateTime;

}
