Apartment
CreateApartment
GetApartment
ListApartment
UpdateApartment

ParkingSpot
CreateParkingSpot
GetParkingSpot
ListParkingSpot
UpdateParkingSpot

Buildings
CreateBuilding
GetBuilding
ListBuilding
UpdateBuilding

GlobalReceipt
UpdateGlobalReceipt
ListGlobalReceipt
GetGlobalReceipt
CreateGlobalReceipt

GlobalReceiptExtraordinary
UpdateGlobalReceiptExtraordinary
ListGlobalReceiptExtraordinary
GetGlobalReceiptExtraordinary
CreateGlobalReceiptExtraordinary

BuildingReceipt
UpdateBuildingReceipt
ListBuildingReceipt
GetBuildingReceipt
CreateBuildingReceipt

BuildingReceiptExtraordinary
UpdateBuildingReceiptExtraordinary
ListBuildingReceiptExtraordinary
GetBuildingReceiptExtraordinary
CreateBuildingReceiptExtraordinary

WaterConsuption
UpdateWaterConsuption
ListWaterConsuption
GetWaterConsuption
CreateWaterConsuption

Period
ListPeriod
GetPeriod

CEO
|--Crear Condominium
|--Crear User con rol ADMINISTRADOR
    |
    ↓
    Administrador del Condominio
    |
    |-- Crear Users(Owner Inicialmente)
    |
    ↓
    Owner
    ↓
    Crear Ownership

Administrador
User
CreateUser
ListUser
UpdateUser
GetUser
NoDelete
NoSoftDelete


Ownership
CreateOwnership
GetOwnership
ListOwnership
UpdateOwnership
* EndOwnership(Caso particular de UpdateOwnership)

PropertyUnit
CreatePropertyUnit
GetPropertyUnit
ListPropertyUnit
UpdatePropertyUnit

Seleccionar Periodo
        ↓
Generar Recibo Automaticamente


Create GlobalReceipt
        ↓
Create BuildingReceipt
        ↓
GenerateReceipts
        ↓
Se detecta el error
        ↓
VoidAllReceipts
        ↓
    Corregir
        ↓
Generar Receipt Nuevamente

Receipt
* GenerateReceipt
GetReceipt(Ver un recibo en especifico)
ListReceipt
No Delete
* VoidReceipt (Recibo Nulo)
* VoidAllReceipt


Propietario
        ↓
Sube voucher.jpg
        ↓
Registro pago
        ↓
Administrador revisa
        ↓
Aprueba o Rechaza

PaymentStatus
Voucher enviado             Voucher enviado
        ↓                       ↓
Pendiente de revision       Pendiente de revision
        ↓                       ↓
    Aprobado                Rechazado


Payment
* RegisterPayment (El administrador registra un pago)
GetPayment
ListPayment
UpdatePayment
* ApprovePayment
* RejectPayment

PaymentReceipt
GetPaymentReceipt
LisPaymentReceipts
    

Condominiums
No existe interfaz