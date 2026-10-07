package com.cndmnio.HabitaOps.persistence.entity.property;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "parking_spots",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"number", "condominium_id"})
        })
@Getter
@Setter
@NoArgsConstructor
public class ParkingSpot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String number;
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal area;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "condominium_id", nullable = false)
    private Condominium condominium;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property_unit_id")
    private PropertyUnit propertyUnit;


}
