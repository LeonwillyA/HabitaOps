package com.cndmnio.HabitaOps.persistence.entity;

import com.cndmnio.HabitaOps.persistence.entity.property.PropertyUnit;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ownerships")
@Getter
@Setter
public class Ownership {
    //Aqui va la relación humana, propietario, copropietarios, inquilinos, etc

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Dueño
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property_unit_id", nullable = false)
    private PropertyUnit propertyUnit;
}
