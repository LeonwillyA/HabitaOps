package com.cndmnio.HabitaOps.service;

import com.cndmnio.HabitaOps.persistence.entity.enums.PropertyUnitStatus;
import com.cndmnio.HabitaOps.persistence.entity.property.Building;
import com.cndmnio.HabitaOps.persistence.entity.property.Condominium;
import com.cndmnio.HabitaOps.persistence.entity.property.ParkingSpot;
import com.cndmnio.HabitaOps.persistence.entity.property.PropertyUnit;
import com.cndmnio.HabitaOps.persistence.repository.PropertyUnitRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Cálculo de los factores de participación de {@link PropertyUnit} (RC-PU-001..003).
 *
 * <p>Los tres valores quedan persistidos en la entidad: {@code area} (bruta, escala 2) y los
 * dos factores ({@code buildingAreaFactor} y {@code condominiumAreaFactor}, escala 4).</p>
 */
@Service
@RequiredArgsConstructor
public class PropertyUnitService {

    private static final int FACTOR_SCALE = 4;
    private static final BigDecimal ZERO_FACTOR = BigDecimal.ZERO.setScale(FACTOR_SCALE, RoundingMode.HALF_UP);

    private final PropertyUnitRepository propertyUnitRepository;

    /**
     * Recalcula area, buildingAreaFactor y condominiumAreaFactor de todas las unidades
     * de un condominio.
     */
    @Transactional
    public void recalculateForCondominium(Long condominiumId) {
        List<PropertyUnit> units = propertyUnitRepository.findActiveByCondominiumId(condominiumId);
        recalculate(units);
    }

    /**
     * Recalcula el condominio al que pertenece la unidad. Como los factores de todas las
     * unidades dependen del área total, se recalcula el condominio completo.
     */
    @Transactional
    public void recalculateForUnit(Long propertyUnitId) {
        PropertyUnit unit = propertyUnitRepository.findById(propertyUnitId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "PropertyUnit no encontrada: " + propertyUnitId));
        if (unit.getStatus() != PropertyUnitStatus.ACTIVE) {
            throw new IllegalStateException(
                    "La PropertyUnit " + propertyUnitId + " no está ACTIVOA (status = "
                            + unit.getStatus() + "): las unidades cerradas no se recalculan.");
        }
        Condominium condominium = condominiumOf(unit);
        if (condominium == null) {
            throw new IllegalStateException(
                    "La PropertyUnit " + propertyUnitId + " no tiene Apartment ni ParkingSpot (BR-PU-001).");
        }
        recalculateForCondominium(condominium.getId());
    }

    private void recalculate(List<PropertyUnit> units) {
        if (units.isEmpty()) {
            return;
        }

        // 1. Calcular el área de cada unidad y acumular el total del condominio (RC-PU-001).
        BigDecimal condominiumTotal = BigDecimal.ZERO;
        for (PropertyUnit unit : units) {
            BigDecimal area = computeArea(unit).setScale(2, RoundingMode.HALF_UP);
            unit.setArea(area);
            condominiumTotal = condominiumTotal.add(area);
        }
        if (condominiumTotal.signum() == 0) {
            throw new IllegalStateException(
                    "El área total del condominio es cero: no se pueden calcular los factores.");
        }

        // 2. Total de área por Building (denominador de RC-PU-002).
        Map<Long, BigDecimal> buildingTotals = new HashMap<>();
        for (PropertyUnit unit : units) {
            Building building = buildingOf(unit);
            if (building != null) {
                buildingTotals.merge(building.getId(), unit.getArea(), BigDecimal::add);
            }
        }

        // 3. Factores (RC-PU-002 y RC-PU-003).
        /** "Para cada unidad: calcula su factor de condominio (área de la unidad ÷ área total del condominio).
         * Si tiene edificio, calcula también su factor de edificio (área de la unidad ÷ área total del edificio).
         * Si no tiene edificio, su factor de edificio es cero." */
        for (PropertyUnit unit : units) {

            BigDecimal area = unit.getArea();
            unit.setCondominiumAreaFactor(divide(area, condominiumTotal));

            Building building = buildingOf(unit);
            if (building == null) {
                unit.setBuildingAreaFactor(ZERO_FACTOR);
            } else {
                unit.setBuildingAreaFactor(divide(area, buildingTotals.get(building.getId())));
            }
        }
    }

    /** RC-PU-001: area = apartment.area + Σ parkingSpot.area. */
    private BigDecimal computeArea(PropertyUnit unit) {
        BigDecimal area = BigDecimal.ZERO;
        if (unit.getApartment() != null) {
            area = area.add(unit.getApartment().getArea());
        }
        if (unit.getParkingSpots() != null) {
            for (ParkingSpot spot : unit.getParkingSpots()) {
                area = area.add(spot.getArea());
            }
        }
        return area;
    }

    private Building buildingOf(PropertyUnit unit) {
        return unit.getApartment() != null ? unit.getApartment().getBuilding() : null;
    }

    private Condominium condominiumOf(PropertyUnit unit) {
        if (unit.getApartment() != null) {
            return unit.getApartment().getBuilding().getCondominium();
        }
        List<ParkingSpot> spots = unit.getParkingSpots();
        if (spots != null && !spots.isEmpty()) {
            return spots.get(0).getCondominium();
        }
        return null;
    }

    private BigDecimal divide(BigDecimal numerator, BigDecimal denominator) {
        return numerator.divide(denominator, FACTOR_SCALE, RoundingMode.HALF_UP);
    }
}
