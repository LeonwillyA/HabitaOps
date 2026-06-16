package com.cndmnio.HabitaOps.persistence.entity;

import com.cndmnio.HabitaOps.persistence.entity.enums.OwnershipType;
import com.cndmnio.HabitaOps.persistence.entity.property.PropertyUnit;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "ownerships")
@Getter
@Setter
@NoArgsConstructor
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

    // 🔥 HISTORIAL
    @Column(nullable = false)
    private LocalDate startDate;

    @Column
    private LocalDate endDate;

    // tipo de relación
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OwnershipType type;

    // opcional pero MUY útil
    @Column(nullable = false)
    private boolean active;
}
