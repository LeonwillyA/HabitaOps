package com.cndmnio.HabitaOps.service;

import com.cndmnio.HabitaOps.persistence.entity.User;
import com.cndmnio.HabitaOps.persistence.entity.WaterConsumption;
import com.cndmnio.HabitaOps.persistence.entity.billing.BuildingReceipt;
import com.cndmnio.HabitaOps.persistence.entity.billing.GlobalReceipt;
import com.cndmnio.HabitaOps.persistence.entity.billing.Period;
import com.cndmnio.HabitaOps.persistence.entity.billing.Receipt;
import com.cndmnio.HabitaOps.persistence.entity.enums.PropertyUnitStatus;
import com.cndmnio.HabitaOps.persistence.entity.enums.ReceiptStatus;
import com.cndmnio.HabitaOps.persistence.entity.property.Building;
import com.cndmnio.HabitaOps.persistence.entity.property.PropertyUnit;
import com.cndmnio.HabitaOps.persistence.repository.BuildingReceiptRepository;
import com.cndmnio.HabitaOps.persistence.repository.GlobalReceiptRepository;
import com.cndmnio.HabitaOps.persistence.repository.PeriodRepository;
import com.cndmnio.HabitaOps.persistence.repository.PropertyUnitRepository;
import com.cndmnio.HabitaOps.persistence.repository.ReceiptRepository;
import com.cndmnio.HabitaOps.persistence.repository.UserRepository;
import com.cndmnio.HabitaOps.persistence.repository.WaterConsumptionRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Generación de recibos por unidad (RC-R-001..006).
 *
 * <p>Dado un Period, cada unidad ACTIVE del condominio recibe un {@link Receipt} con su
 * prorrata global, su prorrata de edificio y su cargo de agua, descontando el saldo a favor
 * previo. Los factores de participación vienen ya persistidos en {@link PropertyUnit}
 * (los calcula {@link PropertyUnitService}).</p>
 */
@Service
@RequiredArgsConstructor
public class ReceiptService {

    private static final int MONEY_SCALE = 2;

    private final ReceiptRepository receiptRepository;
    private final PropertyUnitRepository propertyUnitRepository;
    private final WaterConsumptionRepository waterConsumptionRepository;
    private final PeriodRepository periodRepository;
    private final GlobalReceiptRepository globalReceiptRepository;
    private final BuildingReceiptRepository buildingReceiptRepository;
    private final UserRepository userRepository;

    /**
     * Genera los recibos de todas las unidades ACTIVE de un condominio para un periodo.
     * Saltea las unidades que ya tienen un recibo vigente (BR-R-003, proceso por lote).
     */
    @Transactional
    public List<Receipt> generateForCondominium(Long condominiumId, Long periodId,
            LocalDate issueDate, LocalDate dueDate, Long generatedByUserId) {
        List<PropertyUnit> units = propertyUnitRepository.findActiveByCondominiumId(condominiumId);
        List<Receipt> generated = new ArrayList<>();
        for (PropertyUnit unit : units) {
            if (receiptRepository.existsByPropertyUnitIdAndPeriodIdAndStatusNot(
                    unit.getId(), periodId, ReceiptStatus.VOIDED)) {
                continue;
            }
            generated.add(generate(unit, periodId, issueDate, dueDate, generatedByUserId));
        }
        return generated;
    }

    /** Genera el recibo de una unidad. Falla si ya existe un recibo vigente (BR-R-003). */
    @Transactional
    public Receipt generateForUnit(Long propertyUnitId, Long periodId,
            LocalDate issueDate, LocalDate dueDate, Long generatedByUserId) {
        PropertyUnit unit = propertyUnitRepository.findById(propertyUnitId)
                .orElseThrow(() -> new EntityNotFoundException("PropertyUnit no encontrada: " + propertyUnitId));
        if (receiptRepository.existsByPropertyUnitIdAndPeriodIdAndStatusNot(
                propertyUnitId, periodId, ReceiptStatus.VOIDED)) {
            throw new IllegalStateException(
                    "La PropertyUnit " + propertyUnitId + " ya tiene un recibo vigente para el periodo "
                            + periodId + " (BR-R-003).");
        }
        return generate(unit, periodId, issueDate, dueDate, generatedByUserId);
    }

    private Receipt generate(PropertyUnit unit, Long periodId, LocalDate issueDate,
            LocalDate dueDate, Long generatedByUserId) {
        if (unit.getStatus() != PropertyUnitStatus.ACTIVE) {
            throw new IllegalStateException(
                    "La PropertyUnit " + unit.getId() + " no está ACTIVE (status = " + unit.getStatus()
                            + "): solo las ACTIVE generan recibo (BR-PU-008).");
        }
        LocalDate effectiveIssueDate = issueDate != null ? issueDate : LocalDate.now();
        if (dueDate == null) {
            throw new IllegalStateException("La fecha de vencimiento (dueDate) es obligatoria (BR-R-007).");
        }
        if (dueDate.isBefore(effectiveIssueDate)) {
            throw new IllegalStateException(
                    "dueDate (" + dueDate + ") debe ser igual o posterior a issueDate ("
                            + effectiveIssueDate + ") (BR-R-007).");
        }

        Period period = periodRepository.findById(periodId)
                .orElseThrow(() -> new EntityNotFoundException("Period no encontrado: " + periodId));
        GlobalReceipt globalReceipt = globalReceiptRepository.findByPeriodId(periodId)
                .orElseThrow(() -> new IllegalStateException(
                        "No hay GlobalReceipt para el periodo " + periodId + " (BR-R-004)."));
        User generatedBy = userRepository.findById(generatedByUserId)
                .orElseThrow(() -> new EntityNotFoundException("User no encontrado: " + generatedByUserId));

        Receipt receipt = new Receipt();
        receipt.setPropertyUnit(unit);
        receipt.setPeriod(period);
        receipt.setGlobalReceipt(globalReceipt);
        receipt.setGeneratedBy(generatedBy);
        receipt.setIssueDate(effectiveIssueDate);
        receipt.setDueDate(dueDate);

        // RC-R-002: prorrata global.
        BigDecimal globalShare = round(globalReceipt.getTotalAmount().multiply(unit.getCondominiumAreaFactor()));

        // RC-R-003: prorrata de edificio (0 si la unidad no tiene Building).
        Building building = buildingOf(unit);
        BigDecimal buildingShare = round(BigDecimal.ZERO);
        if (building != null) {
            BuildingReceipt buildingReceipt = buildingReceiptRepository
                    .findByBuildingIdAndPeriodId(building.getId(), periodId)
                    .orElseThrow(() -> new IllegalStateException(
                            "No hay BuildingReceipt para el Building " + building.getId()
                                    + " y periodo " + periodId + " (BR-R-005)."));
            receipt.setBuildingReceipt(buildingReceipt);
            buildingShare = round(buildingReceipt.getTotalAmount().multiply(unit.getBuildingAreaFactor()));
        }

        // RC-R-004: cargo de agua (0 si no hay lectura).
        BigDecimal waterCharge = round(BigDecimal.ZERO);
        WaterConsumption consumption = waterConsumptionRepository
                .findByPropertyUnitIdAndPeriodId(unit.getId(), periodId).orElse(null);
        if (consumption != null) {
            waterCharge = round(consumption.getM3Consumed().multiply(period.getWaterFactor()));
        }

        // RC-R-001: monto bruto.
        BigDecimal grossAmount = round(globalShare.add(buildingShare).add(waterCharge));

        // RC-R-006: descuento del saldo a favor previo (último recibo vigente).
        BigDecimal previousCredit = receiptRepository
                .findTopByPropertyUnitIdAndStatusNotOrderByIssueDateDescIdDesc(unit.getId(), ReceiptStatus.VOIDED)
                .map(Receipt::getCreditBalance)
                .orElse(BigDecimal.ZERO);
        BigDecimal totalAmount = grossAmount.subtract(previousCredit).max(BigDecimal.ZERO)
                .setScale(MONEY_SCALE, RoundingMode.HALF_UP);
        BigDecimal creditBalance = previousCredit.subtract(grossAmount).max(BigDecimal.ZERO)
                .setScale(MONEY_SCALE, RoundingMode.HALF_UP);

        receipt.setGrossAmount(grossAmount);
        receipt.setTotalAmount(totalAmount);
        receipt.setCreditBalance(creditBalance);
        receipt.setStatus(initialStatus(dueDate));

        return receiptRepository.save(receipt);
    }

    private Building buildingOf(PropertyUnit unit) {
        return unit.getApartment() != null ? unit.getApartment().getBuilding() : null;
    }

    private BigDecimal round(BigDecimal value) {
        return value.setScale(MONEY_SCALE, RoundingMode.HALF_UP);
    }

    /** RC-R-005: un recibo nuevo no tiene pagos (paidTotal = 0). */
    private ReceiptStatus initialStatus(LocalDate dueDate) {
        return dueDate.isBefore(LocalDate.now()) ? ReceiptStatus.OVERDUE : ReceiptStatus.PENDING;
    }
}
