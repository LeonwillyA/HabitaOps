package com.cndmnio.HabitaOps.persistence.entity.billing;

import com.cndmnio.HabitaOps.persistence.entity.property.Building;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;


@Entity
@Table(
        name = "building_receipts",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"building_id", "period_id"})
        }
)
@Getter
@Setter
@NoArgsConstructor
public class BuildingReceipt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Gastos de torre/edificio

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal buildingElectricity;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal elevatorMaintenance;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal airExtractorMaintenance;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal fineAmount;//multa

    // Total torre
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "period_id", nullable = false)
    private Period period;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "building_id", nullable = false)
    private Building building;

    @OneToMany(
            mappedBy = "buildingReceipt",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<BuildingReceiptExtraordinary> extraordinaryExpenses;


}