package com.cndmnio.HabitaOps.persistence.repository;

import com.cndmnio.HabitaOps.persistence.entity.billing.PaymentReceipt;
import com.cndmnio.HabitaOps.persistence.entity.enums.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;

public interface PaymentReceiptRepository extends JpaRepository<PaymentReceipt, Long> {

    /**
     * RC-PY-001 / RC-R-005: suma aplicada a un recibo, considerando solo pagos con el
     * status indicado (APPROVED).
     */
    @Query("""
            select coalesce(sum(pr.appliedAmount), 0)
              from PaymentReceipt pr
             where pr.receipt.id = :receiptId
               and pr.payment.status = :status
            """)
    BigDecimal sumAppliedAmountByReceiptIdAndPaymentStatus(
            @Param("receiptId") Long receiptId,
            @Param("status") PaymentStatus status);

    /**
     * RC-PY-001: suma ya aplicada de un pago, para saber cuánto de su amountPaid
     * queda por repartir.
     */
    @Query("""
            select coalesce(sum(pr.appliedAmount), 0)
              from PaymentReceipt pr
             where pr.payment.id = :paymentId
            """)
    BigDecimal sumAppliedAmountByPaymentId(@Param("paymentId") Long paymentId);
}
