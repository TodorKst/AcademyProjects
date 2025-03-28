package org.example.medicalrecordproject.repositories;

import org.example.medicalrecordproject.models.Diagnosis;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiagnosisRepository extends JpaRepository<Diagnosis, Integer> {
}
