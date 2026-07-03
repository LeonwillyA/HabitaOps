package com.cndmnio.HabitaOps.persistence.entity.billing;


import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;


@Entity
@Table(name = "global_receipt_extraordinary")
@Getter
@Setter
@NoArgsConstructor
public class GlobalReceiptExtraordinary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Ejemplo:
    // Pintado de fachada
    // Cambio de bomba
    // Reparación de cisterna
    @Column(nullable = false, length = 255)
    private String concept;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "global_receipt_id", nullable = false)
    private GlobalReceipt globalReceipt;
}
