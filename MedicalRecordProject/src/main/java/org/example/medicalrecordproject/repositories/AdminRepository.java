package org.example.medicalrecordproject.repositories;

import org.example.medicalrecordproject.models.users.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Long> {

    Optional<Admin> findById(long id);

    boolean existsByUsername(String username);

}
