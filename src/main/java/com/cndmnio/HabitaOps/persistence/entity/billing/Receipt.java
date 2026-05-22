package com.cndmnio.HabitaOps.persistence.entity.billing;

import com.cndmnio.HabitaOps.persistence.entity.property.PropertyUnit;
import com.cndmnio.HabitaOps.persistence.entity.WaterConsumption;
import com.cndmnio.HabitaOps.persistence.entity.enums.ReceiptStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "receipts")
@Getter
@Setter
public class Receipt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Totales y estado final

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @Column(nullable = false)
    private LocalDate issueDate;

    @Column(nullable = false)
    private LocalDate dueDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReceiptStatus status;

    // Unidad/departamento al que pertenece el recibo

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property_unit_id", nullable = false)
    private PropertyUnit propertyUnit;

    // Periodo del recibo

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "period_id", nullable = false)
    private Period period;

    // Componentes internos del recibo

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "global_receipt_id", nullable = false)
    private GlobalReceipt globalReceipt;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "building_receipt_id", nullable = false)
    private BuildingReceipt buildingReceipt;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "water_consumption_id", nullable = false)
    private WaterConsumption waterConsumption;
}
