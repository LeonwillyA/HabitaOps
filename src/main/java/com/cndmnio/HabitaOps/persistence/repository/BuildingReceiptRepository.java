package com.cndmnio.HabitaOps.persistence.repository;

import com.cndmnio.HabitaOps.persistence.entity.billing.BuildingReceipt;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BuildingReceiptRepository extends JpaRepository<BuildingReceipt, Long> {

    /** BR-BR-003: un BuildingReceipt por combinación de Building y Period. */
    Optional<BuildingReceipt> findByBuildingIdAndPeriodId(Long buildingId, Long periodId);
}
