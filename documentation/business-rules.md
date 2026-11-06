# REGLAS DE NEGOCIO

## PropertyUnit
BR-PU-001: Toda PropertyUnit debe tener al menos un bien inmobiliario asociado (Apartment o ParkingSpot).
BR-PU-002: Un Apartment no puede estar asociado simultáneamente a más de una PropertyUnit.
BR-PU-003: Un ParkingSpot no puede estar asociado simultáneamente a más de una PropertyUnit.
BR-PU-004: Una PropertyUnit puede estar conformada por cero o un Apartment y por cero o más ParkingSpot.
BR-PU-005: El área total de la PropertyUnit debe ser mayor que cero.
BR-PU-006: Los factores de participación (buildingAreaFactor y condominiumAreaFactor) deben ser mayores que cero, salvo buildingAreaFactor, que es cero cuando la PropertyUnit no tiene Apartment (solo ParkingSpot, sin Building).
BR-PU-007: Una PropertyUnit en estado CLOSED no puede generar nuevos recibos, registrar nuevos consumos ni establecer nuevas relaciones de Ownership.
BR-PU-008: Solo las PropertyUnit ACTIVE participan en el cálculo de los factores de participación (RC-PU-002 y RC-PU-003). Las CLOSED no cuentan como denominador y mantienen su area y sus factores congelados.

## Apartment
BR-AP-001: Todo Apartment debe estar asociado a un Building.
BR-AP-002: El número de Apartment debe ser único dentro de un Building.
BR-AP-003: El área del Apartment debe ser mayor que cero.
BR-AP-004: Todo Apartment debe tener un piso asociado.
BR-AP-005: La eliminación de un Apartment no está permitida.
BR-AP-006: Todo Apartment debe estar asociado a una PropertyUnit.

## ParkingSpot
BR-PS-001: El número de ParkingSpot debe ser único dentro del Condominio.
BR-PS-002: El área del ParkingSpot debe ser mayor que cero.
BR-PS-003: La eliminación de un ParkingSpot no está permitida.
BR-PS-004: Todo ParkingSpot debe formar parte de una PropertyUnit.


## Condominium
BR-CO-001: Todo Condominium debe tener un nombre y una dirección.
BR-CO-002: El nombre del Condominium debe ser único.
BR-CO-003: La eliminación de un Condominium no está permitida.

## Building
BR-BD-001: Todo Building debe estar asociado a un Condominium.
BR-BD-002: El nombre del Building debe ser único dentro de un Condominium.
BR-BD-003: La eliminación de un Building no está permitida.

## User
BR-US-001: Todo User debe tener nombre, numero de celular, email y password.
BR-US-002: El email del User debe ser único.
BR-US-003: Todo User debe tener un Role.
BR-US-004: El Role define qué puede hacer el usuario en el sistema (global), a diferencia de OwnershipType, que define su relación con una unidad.
BR-US-005: Un User con active = false no puede operar en el sistema.

## Ownership
BR-OW-001: Todo Ownership debe estar asociado a un User.
BR-OW-002: Todo Ownership debe estar asociado a una PropertyUnit.
BR-OW-003: Todo Ownership debe tener una fecha de inicio (startDate).
BR-OW-004: Si existe fecha de fin (endDate), debe ser igual o posterior a la fecha de inicio.
BR-OW-005: Pueden existir varios Ownership activos sobre la misma PropertyUnit (OWNER, CO_OWNER, TENANT). Ownership es un registro de las personas relacionadas con la unidad, no un título de propiedad.
BR-OW-006: Ownership no registra porcentaje de participación (share). La transferencia de un porcentaje se refleja cerrando un registro (endDate) y abriendo otro.

## GlobalReceipt
BR-GR-001: Todo GlobalReceipt debe pertenecer a un Period y solo puede existir uno por cada Period.
BR-GR-002: Todos los conceptos económicos de un GlobalReceipt deben ser mayores o iguales a cero.
BR-GR-003: Un GlobalReceipt no puede ser modificado o eliminado mientras existan Receipts vigentes asociados.

## BuildingReceipt
BR-BR-001: Todo BuildingReceipt debe pertenecer a un Building.
BR-BR-002: Todo BuildingReceipt debe pertenecer a un Period.
BR-BR-003: Solo puede existir un BuildingReceipt por combinación de Building y Period.
BR-BR-004: Todos los conceptos económicos de un BuildingReceipt deben ser mayores o iguales a cero.
BR-BR-005: Un BuildingReceipt no puede ser modificado o eliminado mientras existan Receipts vigentes asociados.


## GlobalReceiptExtraordinary
BR-GRE-001: Todo GlobalReceiptExtraordinary debe pertenecer a un GlobalReceipt.
BR-GRE-002: Todo GlobalReceiptExtraordinary debe tener una descripción(concept).
BR-GRE-003: El monto debe ser mayor que cero.
BR-GRE-004: Un GlobalReceiptExtraordinary no puede ser modificado o eliminado mientras existan Receipts vigentes asociados.

## BuildingReceiptExtraordinary
BR-BRE-001: Todo BuildingReceiptExtraordinary debe pertenecer a un BuildingReceipt.
BR-BRE-002: Todo BuildingReceiptExtraordinary debe tener una descripción(concept).
BR-BRE-003: El monto debe ser mayor que cero.
BR-BRE-004: Un BuildingReceiptExtraordinary no puede ser modificado o eliminado mientras existan Receipts vigentes asociados.

## Period
BR-PE-001: El código del Period debe ser único.
BR-PE-002: Todo Period debe tener un waterFactor mayor que cero.
BR-PE-003: El waterFactor de un Period no puede ser modificado mientras existan Receipts vigentes asociados al Period.

## WaterConsumption
BR-WC-001: Solo puede existir un WaterConsumption por combinación de PropertyUnit y Period.
BR-WC-002: La lectura actual debe ser mayor o igual que la lectura anterior.
BR-WC-003: Un WaterConsumption no puede ser modificado o eliminado mientras existan Receipts vigentes asociados.


## Receipt
BR-R-001: Todo Receipt debe pertenecer a una PropertyUnit.
BR-R-002: Todo Receipt debe pertenecer a un Period.
BR-R-003: Solo puede existir un Receipt VIGENTE por combinación de PropertyUnit y Period. (Vigente = status distinto de VOIDED)
BR-R-004: Todo Receipt debe estar asociado a un GlobalReceipt.
BR-R-005: Todo Receipt debe estar asociado a un BuildingReceipt, salvo las unidades sin Building (solo ParkingSpot), donde es nulo.
BR-R-006: El totalAmount y el grossAmount del Receipt deben ser mayores o iguales que cero.
BR-R-007: La fecha de vencimiento (dueDate) debe ser igual o posterior a la fecha de emisión (issueDate).
BR-R-008: Todo Receipt debe tener un usuario generador asociado.
BR-R-009: Un Receipt generado no puede ser modificado.
BR-R-010: Un Receipt erróneo debe pasar a estado VOIDED y ser reemplazado por un nuevo Receipt.
BR-R-011: Un Receipt en estado VOIDED no puede registrar nuevos pagos.
BR-R-012: Al anularse un Receipt (status = VOIDED), la combinación (PropertyUnit, Period) queda libre para emitir un nuevo Receipt.
BR-R-013: El status del Receipt se calcula desde la suma de los appliedAmount de sus pagos APPROVED y de su fecha de vencimiento (sin pagos y no vencido = PENDING, sin pagos y vencido = OVERDUE, parcial = PARTIALLY_PAID, completo = PAID). No se mantiene a mano.
BR-R-014: Al eliminarse o rechazarse un Payment, los Receipts afectados recalculan su status.
BR-R-015: Al emitir un nuevo Receipt, si la unidad tiene saldo a favor (creditBalance) mayor que cero, este se resta del monto a pagar del recibo. Si el creditBalance supera el grossAmount del recibo, el monto a pagar es cero y el creditBalance restante queda registrado en el recibo.


## Payment
BR-PY-001: Al registrar un Payment se indica la PropertyUnit, que determina a qué recibos se aplica (los vigentes de esa unidad). La unidad no se almacena en el Payment: se deduce de sus recibos.
BR-PY-002: El operationNumber no puede repetirse dentro de un mismo banco (o entidad que recibe el dinero).
BR-PY-003: De un Payment se puede aplicar como máximo su amountPaid. La suma de sus appliedAmount no puede superarla.
BR-PY-004: Si el amountPaid supera la deuda vigente del recibo, se aplica solo hasta cubrir la deuda y el resto queda como saldo a favor (creditBalance) al recibo.
BR-PY-005: Un Payment solo afecta a los recibos cuando su status es APPROVED. Mientras está PENDING_VALIDATION o REJECTED no modifica ningún recibo.
BR-PY-006: Un Payment se aplica a los recibos de la unidad del más antiguo al más reciente.
BR-PY-007: El proofFileUrl solo almacena la ruta del comprobante. No es único y no se usa para detectar duplicados.
BR-PY-008: No se registra quién realiza el pago. Puede pagar un tercero ajeno a la unidad; lo único que importa es a qué PropertyUnit corresponde el pago.


## PaymentReceipt
BR-PR-001: Un Payment puede estar asociado a uno o varios Receipts.
BR-PR-002: Un Receipt puede estar asociado a uno o varios Payments.
BR-PR-003: No puede repetirse la combinación (Payment, Receipt).
BR-PR-004: El appliedAmount debe ser mayor que cero.
BR-PR-005: La suma de los appliedAmount de un Receipt no puede superar su totalAmount.
