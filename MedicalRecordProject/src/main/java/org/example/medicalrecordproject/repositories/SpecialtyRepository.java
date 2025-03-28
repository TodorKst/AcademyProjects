package org.example.medicalrecordproject.repositories;

import org.example.medicalrecordproject.models.Specialty;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpecialtyRepository extends JpaRepository<Specialty, Long> {
}
