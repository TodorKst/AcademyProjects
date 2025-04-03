package org.example.medicalrecordproject.services.contracts;

import org.example.medicalrecordproject.dtos.in.creation.PatientCreationDto;
import org.example.medicalrecordproject.dtos.out.GpPatientCountOutDto;
import org.example.medicalrecordproject.dtos.out.PatientOutDto;
import org.example.medicalrecordproject.dtos.out.creationresponse.PatientCreationResponseDto;
import org.example.medicalrecordproject.exceptions.EntityNotFoundException;
import org.example.medicalrecordproject.models.users.Patient;

import java.sql.Timestamp;
import java.util.List;

public interface PatientService {
    List<Patient> getAllPatients();

    Patient getPatientById(long id) throws EntityNotFoundException;

    PatientCreationResponseDto createPatient(PatientCreationDto patientDto, Timestamp createdAt);

    Patient savePatient(Patient patient);

    void deletePatient(long id) throws EntityNotFoundException;

    void updatePatient(long id, Patient patient) throws EntityNotFoundException;

    void payInsurance(long id);

    List<PatientOutDto> getPatientsByDiagnosis(String diagnosisName);

    List<PatientOutDto> getPatientsByGp(Long gpId);

    List<GpPatientCountOutDto> countPatientsPerGp();
}
