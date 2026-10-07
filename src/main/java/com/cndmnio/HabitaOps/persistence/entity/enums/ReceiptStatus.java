package com.cndmnio.HabitaOps.persistence.entity.enums;

public enum ReceiptStatus {
    PENDING,        // no pagado nada
    PARTIALLY_PAID, // pagado parcialmente
    PAID,           // pagado completo
    OVERDUE,        // vencido sin pagar
    VOIDED          //erroneo - anulado
}
