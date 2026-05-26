package com.cndmnio.HabitaOps.persistence.entity.billing;

import com.cndmnio.HabitaOps.persistence.entity.enums.PaymentMethod;
import com.cndmnio.HabitaOps.persistence.entity.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "payments")
@Getter
@Setter
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Monto enviado por el vecino

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amountPaid;

    // Fecha del pago realizado

    @Column(nullable = false)
    private LocalDate paymentDate;

    //Nombre del banco de donde se hace la transferencia
    @Column(length = 100)
    private String bankName;

    //Numero de operacion que indica el voucher
    @Column(length = 100)
    private String operationNumber;

    // Evidencia (imagen/pdf), se guardar la direccion de donde se guarda la imagen, es malo guardar la imagen en la misma BD

    @Column(nullable = false, length = 500)
    private String proofFileUrl;

    //Metodo de pago
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    // Estado de validación

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status;

    @OneToMany(mappedBy = "payment")
    private List<PaymentReceipt> paymentReceipts;

    // Observación del admin (opcional)
    //Si en caso es rechazada el comprobante, se le tiene que indicar el xq
    @Column(length = 500)
    private String observation;


}