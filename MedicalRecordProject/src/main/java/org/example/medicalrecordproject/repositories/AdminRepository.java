package org.example.medicalrecordproject.repositories;

import org.example.medicalrecordproject.models.users.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin, Long> {

}
