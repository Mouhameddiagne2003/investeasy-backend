package com.dic2.investeasy.repository;

import com.dic2.investeasy.domain.User;
import com.dic2.investeasy.domain.UserRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    Page<User> findByEmailContaining(String email, Pageable pageable);
    Page<User> findByRole(UserRole role, Pageable pageable);
    Page<User> findByEmailContainingAndRole(String email, UserRole role, Pageable pageable);
    long countByCreatedAtAfter(LocalDateTime date);
    long countByCreatedAtBefore(LocalDateTime date);
} 