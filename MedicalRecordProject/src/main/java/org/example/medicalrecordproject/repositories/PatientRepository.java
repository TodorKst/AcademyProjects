package org.example.medicalrecordproject.repositories;

import org.example.medicalrecordproject.models.users.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, Long> {

    @Query("""
            SELECT DISTINCT p
            FROM Patient p
            JOIN p.medicalVisits mv
            JOIN mv.diagnoses d
            WHERE d.name = :diagnosisName
            """)
    List<Patient> findByDiagnosisName(@Param("diagnosisName") String diagnosisName);

    List<Patient> findByGpId(Long gpId);

    Optional<Patient> findByUsername(String username);

    @Query("SELECT p.gp.id, COUNT(p) FROM Patient p GROUP BY p.gp.id")
    List<Object[]> countPatientsByGp();

}
