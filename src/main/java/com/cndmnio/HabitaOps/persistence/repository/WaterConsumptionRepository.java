package com.cndmnio.HabitaOps.persistence.repository;

import com.cndmnio.HabitaOps.persistence.entity.WaterConsumption;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WaterConsumptionRepository extends JpaRepository<WaterConsumption, Long> {

    /** RC-R-004: lectura de agua de una unidad en un periodo. */
    Optional<WaterConsumption> findByPropertyUnitIdAndPeriodId(Long propertyUnitId, Long periodId);
}
