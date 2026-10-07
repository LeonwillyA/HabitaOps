# ADR-001

Fecha: 2026-08-06

## Decisión

Utilizar PostgreSQL como base de datos.

## Razón

Permite:
- relaciones complejas
- integridad referencial
- escalabilidad futura



# ADR-002

Fecha: 2026-09-28

## Decisión

Apartment y ParkingSpot serán entidades independientes.

## Razón

Una cochera puede existir sin departamento.
Ambos tienen reglas y atributos diferentes.

# ADR-003

Fecha: 2026-09-25

## Decisión

La regla BR-R-003 se valida en la capa de servicio, no con una constraint
en la base de datos.

## Razón

La unicidad depende del estado del recibo (excluir los VOIDED). JPA no puede
expresar esa condición. Se evaluó un índice único parcial en PostgreSQL y se
postergó: la base de datos tiene un solo responsable, y los recibos se generan
automáticamente.

## Consecuencia

Si en el futuro se requiere garantía a nivel de BD, agregar:
CREATE UNIQUE INDEX ... WHERE status <> 'VOIDED';


# ADR-004

Fecha: 2026-09-28

## Decisión

El Payment no guarda la PropertyUnit ni quién pagó. El pago se aplica a la deuda
de la unidad, deducida de los recibos a los que se aplica. Un pago se aplica a los
recibos de la unidad del más antiguo al más reciente; el sobrante queda como saldo
a favor.

La unicidad del comprobante se garantiza en la BD con una constraint UNIQUE
compuesta (bankName, operationNumber): un mismo banco no repite número de
operación, pero dos bancos distintos sí pueden coincidir.

## Razón

El pago corresponde a la unidad, no a una persona: puede pagar un tercero ajeno a
la unidad. La unidad se deduce de los recibos, no se almacena en el Payment.

La unicidad es por banco, no global: el número lo emite cada banco de forma
independiente. Una UNIQUE global sobre operationNumber rechazaría de más, por eso
la constraint es compuesta.

## Consecuencia

En CASH no hay banco: bankName lleva la entidad que recibe el dinero
(administración o junta directiva) y operationNumber lleva el número único del
recibo físico que se entrega.
