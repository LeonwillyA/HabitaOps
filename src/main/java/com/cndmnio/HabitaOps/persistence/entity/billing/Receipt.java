package com.cndmnio.HabitaOps.persistence.entity.billing;

import com.cndmnio.HabitaOps.persistence.entity.User;
import com.cndmnio.HabitaOps.persistence.entity.property.PropertyUnit;
import com.cndmnio.HabitaOps.persistence.entity.WaterConsumption;
import com.cndmnio.HabitaOps.persistence.entity.enums.ReceiptStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "receipts")
@Getter
@Setter
@NoArgsConstructor
public class Receipt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Totales y estado final

    // Monto bruto del recibo (sin descontar el creditBalance).
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal grossAmount;

    // Monto a pagar (neto): grossAmount descontando el creditBalance.
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;

    // Saldo a favor de la unidad ligado a este recibo. No forma parte del totalAmount
    // ni de lo cobrado vía Payment; sirve para devoluciones (gestión manual).
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal creditBalance = BigDecimal.ZERO;

    @Column(nullable = false)
    private LocalDate issueDate;

    @Column(nullable = false)
    private LocalDate dueDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReceiptStatus status;

    @OneToMany(mappedBy = "receipt")
    private List<PaymentReceipt> paymentReceipts;

    //Quien hace el recibo
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "generated_by_user_id", nullable = false)
    private User generatedBy;

    // Unidad/departamento al que pertenece el recibo

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property_unit_id", nullable = false)
    private PropertyUnit propertyUnit;

    // Periodo del recibo

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "period_id", nullable = false)
    private Period period;

    // Componentes internos del recibo

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "global_receipt_id", nullable = false)
    private GlobalReceipt globalReceipt;

    // Nulo cuando la unidad no tiene Building (solo ParkingSpot): no paga prorrata de torre (D16).
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "building_receipt_id")
    private BuildingReceipt buildingReceipt;


}
