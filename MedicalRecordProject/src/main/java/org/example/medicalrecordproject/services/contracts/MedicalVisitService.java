package org.example.medicalrecordproject.services.contracts;

import org.example.medicalrecordproject.exceptions.EntityNotFoundException;
import org.example.medicalrecordproject.models.MedicalVisit;

import java.time.LocalDateTime;
import java.util.List;

public interface MedicalVisitService {
    List<MedicalVisit> getAllMedicalVisits();

    MedicalVisit getMedicalVisitById(long id) throws EntityNotFoundException;

    MedicalVisit saveMedicalVisit(MedicalVisit medicalVisit);

    void deleteMedicalVisit(long id);

    void updateMedicalVisit(long id, MedicalVisit medicalVisit) throws EntityNotFoundException;

    List<MedicalVisit> getByPatientId(long id);

    List<MedicalVisit> getByDoctorId(long id);

    List<MedicalVisit> getByVisitDate(String date);

    List<MedicalVisit> getByVisitDateBetween(LocalDateTime startDate, LocalDateTime endDate);
}
