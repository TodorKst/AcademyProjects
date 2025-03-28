package org.example.medicalrecordproject.repositories;

import org.example.medicalrecordproject.models.SickLeave;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SickLeaveRepository extends JpaRepository<SickLeave, Long> {
}
