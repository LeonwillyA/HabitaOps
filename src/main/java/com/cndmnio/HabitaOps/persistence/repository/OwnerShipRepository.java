package com.cndmnio.HabitaOps.persistence.repository;

import com.cndmnio.HabitaOps.persistence.entity.Ownership;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OwnerShipRepository extends JpaRepository<Ownership, Long> {
}
