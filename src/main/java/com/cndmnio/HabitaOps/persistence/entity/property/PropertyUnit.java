package com.cndmnio.HabitaOps.persistence.entity.property;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "property_units")
@Getter
@Setter
@NoArgsConstructor
public class PropertyUnit {//Modelo de responsabilidad de pago
    //->Conjunto cobrale / responsable - No necesariamente propiedad legal


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private BigDecimal area;

    //Para saber el porcentaje que se pagara por cada recibo
    @Column(nullable = false, precision = 10, scale = 4)
    private BigDecimal areaFactor;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PropertyUnitStatus status;


    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "apartment_id", unique = true)
    private Apartment apartment;

    @OneToMany(mappedBy = "propertyUnit")
    private List<ParkingSpot> parkingSpots;




}