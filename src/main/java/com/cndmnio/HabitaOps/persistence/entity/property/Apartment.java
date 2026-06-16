package com.cndmnio.HabitaOps.persistence.entity.property;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "apartments",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"number", "building_id"})
        }
)
@Getter
@Setter
@NoArgsConstructor
public class Apartment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String number;
    @Column(nullable = false)
    private Integer floor;
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal area;
    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal participationPercentage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "building_id", nullable = false)
    private Building building;
}
