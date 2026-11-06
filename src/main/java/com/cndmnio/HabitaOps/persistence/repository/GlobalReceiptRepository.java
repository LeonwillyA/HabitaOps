package com.cndmnio.HabitaOps.persistence.repository;

import com.cndmnio.HabitaOps.persistence.entity.billing.GlobalReceipt;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GlobalReceiptRepository extends JpaRepository<GlobalReceipt, Long> {

    /** BR-GR-001: un GlobalReceipt por Period. */
    Optional<GlobalReceipt> findByPeriodId(Long periodId);
}
