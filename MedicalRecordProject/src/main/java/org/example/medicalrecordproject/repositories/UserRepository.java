package org.example.medicalrecordproject.repositories;

import org.example.medicalrecordproject.enums.UserRole;
import org.example.medicalrecordproject.models.users.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    List<User> findAllByRole(UserRole role);

    Optional<User> findByIdAndRole(Long id, UserRole role);

}
