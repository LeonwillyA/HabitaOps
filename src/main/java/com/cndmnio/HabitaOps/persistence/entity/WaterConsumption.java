package com.cndmnio.HabitaOps.persistence.entity;

import com.cndmnio.HabitaOps.persistence.entity.billing.Period;
import com.cndmnio.HabitaOps.persistence.entity.property.PropertyUnit;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "water_consumption")
@Getter
@Setter
public class WaterConsumption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal previousReading;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal currentReading;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal m3Consumed;

    @Column(nullable = false)
    private LocalDate readingDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property_unit_id", nullable = false)
    private PropertyUnit propertyUnit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "period_id", nullable = false)
    private Period period;

}