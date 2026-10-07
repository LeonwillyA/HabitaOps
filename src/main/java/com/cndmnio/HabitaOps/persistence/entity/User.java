package com.cndmnio.HabitaOps.persistence.entity;


import com.cndmnio.HabitaOps.persistence.entity.enums.Role;
import jakarta.persistence.*;
        import lombok.*;

        import java.util.List;


@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    // Saber si sigue con nosotros
    @Column(nullable = false)
    private boolean active;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Ownership> ownerships;


}
