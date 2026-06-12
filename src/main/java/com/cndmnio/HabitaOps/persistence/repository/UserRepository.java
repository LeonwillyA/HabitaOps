package com.cndmnio.HabitaOps.persistence.repository;

import com.cndmnio.HabitaOps.persistence.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
