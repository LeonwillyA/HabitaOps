package com.cndmnio.HabitaOps.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "global_receipts")
@Getter
@Setter
public class GlobalReceipt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Periodo del recibo
    @Column(nullable = false)
    private LocalDate period;

    // Gastos generales del condominio

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal potableWaterPumpMaintenance;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal septicTankMaintenance;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal condominiumElectricity;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal firePumpMaintenance;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal gardening;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal commonAreaWater;

    // Total global

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;

    // Administrador que generó el recibo global

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property_unit_id", nullable = false)
    private PropertyUnit propertyUnit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "generated_by_user_id", nullable = false)
    private User generatedBy;

    @Column(length = 100)
    private String generatedByNameSnapshot;
}