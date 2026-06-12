package com.cndmnio.HabitaOps.persistence.repository;

import com.cndmnio.HabitaOps.persistence.entity.property.Apartment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApartmentRepository extends JpaRepository<Apartment,Long> {
}
