package org.example.medicalrecordproject.repositories;

import org.example.medicalrecordproject.models.MedicalVisit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface MedicalVisitRepository extends JpaRepository<MedicalVisit, Long> {

    List<MedicalVisit> findByPatientId(long id);

    List<MedicalVisit> findByDoctorId(long id);

    List<MedicalVisit> findByVisitDate(LocalDateTime date);

    List<MedicalVisit> findByVisitDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT mv.doctor.id, COUNT(mv) FROM MedicalVisit mv GROUP BY mv.doctor.id")
    List<Object[]> countVisitsPerDoctor();


    @Query("""
            SELECT mv
            FROM MedicalVisit mv
            WHERE mv.visitDate BETWEEN :start AND :end
            AND (:doctorId IS NULL OR mv.doctor.id = :doctorId)
            """)
    List<MedicalVisit> findByDateRangeAndOptionalDoctor(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            @Param("doctorId") Long doctorId
    );

}
