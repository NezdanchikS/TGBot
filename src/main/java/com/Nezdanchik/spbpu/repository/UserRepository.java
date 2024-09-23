package com.Nezdanchik.spbpu.repository;

import com.Nezdanchik.spbpu.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
