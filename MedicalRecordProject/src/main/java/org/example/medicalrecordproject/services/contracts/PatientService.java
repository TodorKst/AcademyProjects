package org.example.medicalrecordproject.services.contracts;

import org.example.medicalrecordproject.exceptions.EntityNotFoundException;
import org.example.medicalrecordproject.models.users.Patient;

import java.util.List;

public interface PatientService {
    List<Patient> getAllPatients();

    Patient getPatientById(long id) throws EntityNotFoundException;

    Patient savePatient(Patient patient);

    void deletePatient(long id) throws EntityNotFoundException;

    void updatePatient(long id, Patient patient) throws EntityNotFoundException;

    void payInsurance(long id);
}
