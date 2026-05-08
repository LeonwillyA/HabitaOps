package com.cndmnio.HabitaOps.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "property_units")
@Getter
@Setter
public class PropertyUnit {//Modelo de responsabilidad de pago
    //->Conjunto cobrale / responsable - No necesariamente propiedad legal


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "apartment_id")
    private Apartment apartment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parking_spot_id")
    private ParkingSpot parkingSpot;
}