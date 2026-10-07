package com.cndmnio.HabitaOps.persistence.entity.billing;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "payment_receipts",
        uniqueConstraints = @UniqueConstraint(columnNames = {"payment_id", "receipt_id"}))
@Getter
@Setter
@NoArgsConstructor
public class PaymentReceipt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id", nullable = false)
    private Payment payment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receipt_id", nullable = false)
    private Receipt receipt;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal appliedAmount;
}