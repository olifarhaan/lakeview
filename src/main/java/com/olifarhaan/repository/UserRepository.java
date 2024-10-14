package com.olifarhaan.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.olifarhaan.model.User;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByEmail(String email);
}
