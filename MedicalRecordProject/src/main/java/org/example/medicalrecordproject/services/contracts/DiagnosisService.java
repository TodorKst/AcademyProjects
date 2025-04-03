package org.example.medicalrecordproject.services.contracts;

import org.example.medicalrecordproject.dtos.in.creation.DiagnosisCreationDto;
import org.example.medicalrecordproject.dtos.out.DiagnosisStatOutDto;
import org.example.medicalrecordproject.dtos.out.response.DiagnosisResponseDto;
import org.example.medicalrecordproject.exceptions.EntityNotFoundException;
import org.example.medicalrecordproject.models.Diagnosis;

import java.util.List;

public interface DiagnosisService {
    List<DiagnosisResponseDto> getAllDiagnoses();

    Diagnosis getDiagnosisById(long id) throws EntityNotFoundException;

    DiagnosisResponseDto getDiagnosisByIdResponse(long id) throws EntityNotFoundException;

    Diagnosis saveDiagnosis(Diagnosis diagnosis);

    DiagnosisResponseDto createDiagnosis(DiagnosisCreationDto diagnosis);

    void deleteDiagnosis(long id);

    void updateDiagnosis(long id, Diagnosis diagnosis) throws EntityNotFoundException;

    List<DiagnosisStatOutDto> getMostCommonDiagnoses();

}
