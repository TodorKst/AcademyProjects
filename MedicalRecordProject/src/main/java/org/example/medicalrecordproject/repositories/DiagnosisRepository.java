package org.example.medicalrecordproject.repositories;

import org.example.medicalrecordproject.models.Diagnosis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DiagnosisRepository extends JpaRepository<Diagnosis, Long> {

    @Query("""
            SELECT d.name, COUNT(mv)
            FROM Diagnosis d
            JOIN d.medicalVisits mv
            GROUP BY d.name
            ORDER BY COUNT(mv) DESC
            """)
    List<Object[]> findMostCommonDiagnoses();


}
