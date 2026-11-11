package com.cndmnio.HabitaOps.persistence.repository;

import com.cndmnio.HabitaOps.persistence.entity.billing.Receipt;
import com.cndmnio.HabitaOps.persistence.entity.enums.ReceiptStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReceiptRepository extends JpaRepository<Receipt, Long> {

    /** BR-R-003: existe un recibo vigente (status distinto de VOIDED) para (unidad, periodo). */
    boolean existsByPropertyUnitIdAndPeriodIdAndStatusNot(
            Long propertyUnitId, Long periodId, ReceiptStatus status);

    /** Último recibo vigente de una unidad, para leer su saldo a favor previo (RC-R-006). */
    Optional<Receipt> findTopByPropertyUnitIdAndStatusNotOrderByIssueDateDescIdDesc(
            Long propertyUnitId, ReceiptStatus status);

    /** RC-PY-001 / BR-PY-006: recibos vigentes de una unidad, de más antiguo a más reciente. */
    List<Receipt> findByPropertyUnitIdAndStatusNotOrderByIssueDateAscIdAsc(
            Long propertyUnitId, ReceiptStatus status);
}