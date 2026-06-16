package com.cndmnio.HabitaOps.persistence.entity.billing;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;


@Entity
@Table(name = "global_receipts")
@Getter
@Setter
@NoArgsConstructor
public class GlobalReceipt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "period_id", nullable = false)
    private Period period;



}