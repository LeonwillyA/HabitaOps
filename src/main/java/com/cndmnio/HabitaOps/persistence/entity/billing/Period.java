package com.cndmnio.HabitaOps.persistence.entity.billing;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;


@Entity
@Table(name = "periods")
@Getter
@Setter
public class Period {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String code; // 2026-01

    @Column(nullable = false, precision = 10, scale = 4)
    private BigDecimal waterFactor;
}