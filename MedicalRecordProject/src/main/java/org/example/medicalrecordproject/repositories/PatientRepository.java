package org.example.medicalrecordproject.repositories;

import org.example.medicalrecordproject.models.users.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepository extends JpaRepository<Patient, Long> {
}
