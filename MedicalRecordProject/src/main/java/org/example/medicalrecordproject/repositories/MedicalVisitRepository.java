package org.example.medicalrecordproject.repositories;

import org.example.medicalrecordproject.models.MedicalVisit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicalVisitRepository extends JpaRepository<MedicalVisit, Long> {
}
