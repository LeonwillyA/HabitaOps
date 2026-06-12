package com.cndmnio.HabitaOps.persistence.repository;

import com.cndmnio.HabitaOps.persistence.entity.WaterConsumption;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WaterConsumptionRepository extends JpaRepository<WaterConsumption, Long> {
}
