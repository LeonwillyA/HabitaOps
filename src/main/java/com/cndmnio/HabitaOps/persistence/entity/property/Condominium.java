package com.cndmnio.HabitaOps.persistence.entity.property;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "condominiums")
@Getter
@Setter
@NoArgsConstructor  
public class Condominium {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String address;

    @OneToMany(mappedBy = "condominium", fetch = FetchType.LAZY)
    private List<Building> buildings; // List of Buildings

}
