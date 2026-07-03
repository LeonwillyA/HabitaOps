package com.cndmnio.HabitaOps.persistence.entity.billing;


import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;


@Entity
@Table(name = "building_receipt_extraordinary")
@Getter
@Setter
@NoArgsConstructor
public class BuildingReceiptExtraordinary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Ejemplo:
    // Reparación ascensor
    // Pintado de pasillos
    // Cambio de luminarias
    @Column(nullable = false, length = 255)
    private String concept;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "building_receipt_id", nullable = false)
    private BuildingReceipt buildingReceipt;
}