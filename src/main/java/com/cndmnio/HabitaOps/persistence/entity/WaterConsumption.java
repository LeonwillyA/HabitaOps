package com.cndmnio.HabitaOps.persistence.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "water_consumption")
public class WaterConsumption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String period; // 2026-01

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

}