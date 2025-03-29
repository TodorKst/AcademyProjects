package org.example.medicalrecordproject.repositories;

import org.example.medicalrecordproject.models.MedicalVisit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public interface MedicalVisitRepository extends JpaRepository<MedicalVisit, Long> {

    List<MedicalVisit> findByPatientId(long id);

    List<MedicalVisit> findByDoctorId(long id);

    List<MedicalVisit> findByVisitDate(LocalDateTime date);

    List<MedicalVisit> findByVisitDateBetween(LocalDateTime startDate, LocalDateTime endDate);
}
