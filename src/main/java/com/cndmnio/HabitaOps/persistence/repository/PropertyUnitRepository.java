package com.cndmnio.HabitaOps.persistence.repository;

import com.cndmnio.HabitaOps.persistence.entity.property.PropertyUnit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PropertyUnitRepository extends JpaRepository<PropertyUnit, Long> {

    /**
     * Unidades de un condominio: por su Apartment (torre → condominio) o por sus ParkingSpot.
     * RC-PU-003.
     */
    @Query("""
            select distinct pu
              from PropertyUnit pu
              left join pu.apartment a
              left join pu.parkingSpots ps
             where (a.building.condominium.id = :condominiumId
                    or ps.condominium.id = :condominiumId)
                   and pu.status = com.cndmnio.HabitaOps.persistence.entity.enums.PropertyUnitStatus.ACTIVE
            """)
    List<PropertyUnit> findActiveByCondominiumId(@Param("condominiumId") Long condominiumId);


}
