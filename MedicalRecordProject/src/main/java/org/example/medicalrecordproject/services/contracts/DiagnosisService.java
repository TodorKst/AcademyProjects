package org.example.medicalrecordproject.services.contracts;

import org.example.medicalrecordproject.models.Diagnosis;

import java.util.List;

public interface DiagnosisService {
    List<Diagnosis> getAllDiagnoses();

    Diagnosis getDiagnosisById(long id);

    Diagnosis saveDiagnosis(Diagnosis diagnosis);

    void deleteDiagnosis(long id);

    void updateDiagnosis(long id, Diagnosis diagnosis);
}
