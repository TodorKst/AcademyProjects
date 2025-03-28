package org.example.medicalrecordproject.services.contracts;

import org.example.medicalrecordproject.models.MedicalVisit;

import java.util.List;

public interface MedicalVisitService {
    List<MedicalVisit> getAllMedicalVisits();

    MedicalVisit getMedicalVisitById(long id);

    MedicalVisit saveMedicalVisit(MedicalVisit medicalVisit);

    void deleteMedicalVisit(long id);

    void updateMedicalVisit(long id, MedicalVisit medicalVisit);
}
