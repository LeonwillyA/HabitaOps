# REGLAS DE CÁLCULO

Reglas que definen cómo se calculan los valores derivados del sistema.

Los montos monetarios y de consumo que se calculan aquí se calculan al momento de
creación/emisión del registro y quedan persistidos como columnas (valores congelados).
Corregir una lectura o un gasto viejo no reescribe el histórico (ver D4). La única
excepción es `PropertyUnit.area`, que es puramente derivada y puede calcularse en el
getter sin persistirse.

## Convenciones

- Los montos monetarios se redondean a 2 decimales (HALF_UP) después de cada multiplicación.
- `waterFactor` (Period) es una entrada persistida: no se calcula. Los factores de
  participación (`buildingAreaFactor` y `condominiumAreaFactor`) se calculan desde las
  áreas (RC-PU-002 y RC-PU-003).
- La deuda restante de un Receipt es `totalAmount − Σ(appliedAmount de sus pagos APPROVED)`.

## PropertyUnit

RC-PU-001: El área total de la PropertyUnit se calcula como la suma del área de su
Apartment (si tiene) más el área de todos sus ParkingSpot asociados.

    area = apartment.area + Σ(parkingSpot.area)

RC-PU-002: El factor de participación del edificio se calcula como el área de la unidad
sobre el área total de las unidades ACTIVE de su Building.

    buildingAreaFactor = propertyUnit.area / Σ(area de las PropertyUnit ACTIVE del Building)

RC-PU-003: El factor de participación del condominio se calcula como el área de la unidad
sobre el área total de todas las unidades ACTIVE del Condominium.

    condominiumAreaFactor = propertyUnit.area / Σ(area de todas las PropertyUnit ACTIVE del Condominium)

## GlobalReceipt

RC-GR-001: El totalAmount de un GlobalReceipt se calcula como la suma de sus seis
conceptos económicos más la suma de los amount de sus GlobalReceiptExtraordinary.

    totalAmount = potableWaterPumpMaintenance
                + septicTankMaintenance
                + condominiumElectricity
                + firePumpMaintenance
                + gardening
                + commonAreaWater
                + Σ(extraordinary.amount)

## BuildingReceipt

RC-BR-001: El totalAmount de un BuildingReceipt se calcula como la suma de sus cuatro
conceptos económicos más la suma de los amount de sus BuildingReceiptExtraordinary.

    totalAmount = buildingElectricity
                + elevatorMaintenance
                + airExtractorMaintenance
                + fineAmount
                + Σ(extraordinary.amount)

## WaterConsumption

RC-WC-001: El consumo de agua se calcula como la diferencia entre la lectura actual y
la lectura anterior.

    m3Consumed = currentReading − previousReading

## Receipt

RC-R-001: El grossAmount (monto bruto) del Receipt se calcula como la suma de la prorrata
global, la prorrata de edificio y el cargo de agua.

    grossAmount = globalShare + buildingShare + waterCharge

RC-R-002: La prorrata global es la parte del GlobalReceipt que corresponde a la unidad
según su factor de participación del condominio.

    globalShare = globalReceipt.totalAmount × condominiumAreaFactor

RC-R-003: La prorrata de edificio es la parte del BuildingReceipt que corresponde a la
unidad según su factor de participación del edificio.

    buildingShare = buildingReceipt.totalAmount × buildingAreaFactor

RC-R-004: El cargo de agua es el consumo de la unidad multiplicado por el factor de agua
del Period.

    waterCharge = m3Consumed × period.waterFactor

RC-R-005: El status del Receipt se calcula (no se mantiene a mano, BR-R-013) desde la
suma de los appliedAmount de sus pagos APPROVED:

    paidTotal = Σ(appliedAmount de PaymentReceipts con Payment.status = APPROVED)

    paidTotal >= totalAmount        → PAID
    0 < paidTotal < totalAmount     → PARTIALLY_PAID
    paidTotal == 0 y dueDate >= hoy → PENDING
    paidTotal == 0 y dueDate < hoy  → OVERDUE

VOIDED es un estado manual (BR-R-010), no sale de este cálculo.

RC-R-006: Al emitir el Receipt, si la unidad tiene un saldo a favor previo (creditBalance),
este se descuenta del grossAmount:

    totalAmount   = max(0, grossAmount − creditBalancePrevio)
    creditBalance = max(0, creditBalancePrevio − grossAmount)

Si el creditBalance es mayor que el grossAmount, el totalAmount queda en cero y el
excedente se conserva en creditBalance.

## Payment

RC-PY-001: Al aprobarse un Payment, su amountPaid se aplica a los recibos VIGENTES de la
unidad del más antiguo al más reciente (por fecha de emisión, BR-PY-006). Para cada
recibo, el monto aplicado es el mínimo entre su deuda restante y lo que queda del pago:

    por cada Receipt vigente, en orden de issueDate ascendente:
      deuda   = receipt.totalAmount − Σ(appliedAmount ya aplicado a ese recibo)
      aplicado = min(deuda, amountPaid − Σ(appliedAmount ya aplicado del pago))
      si aplicado > 0 → se crea un PaymentReceipt con ese appliedAmount

La suma de los appliedAmount de un Payment nunca supera su amountPaid (BR-PY-003), y la
suma sobre un Receipt nunca supera su totalAmount (BR-PR-005).

RC-PY-002: Si tras cubrir toda la deuda vigente de la unidad queda dinero del pago sin
aplicar, ese sobrante se registra como saldo a favor (creditBalance) del recibo (BR-PY-004).

    creditBalance = amountPaid − Σ(appliedAmount del Payment)

El creditBalance no se suma a los appliedAmount (lo que los vecinos pagan vía Payment).
Se descuenta del monto a pagar al emitir un nuevo recibo (RC-R-006) y también puede
devolverse de forma manual si el propietario lo pide.

## Notas

- `buildingAreaFactor` y `condominiumAreaFactor` son columnas de `PropertyUnit`, calculadas
  por el servicio (RC-PU-002 y RC-PU-003). Se recalculan cuando cambian las áreas o el
  conjunto de unidades. El valor congelado que importa para el histórico es el totalAmount
  del Receipt, no el factor.
- Solo las PropertyUnit ACTIVE participan en el recálculo y en los denominadores de
  RC-PU-002/003. Las CLOSED no cuentan y no se recalculan: su area y sus factores quedan
  congelados.
- `grossAmount` (monto bruto) y `totalAmount` (monto a pagar, neto) se persisten ambos.
  El `creditBalance` es el saldo a favor restante del recibo.
