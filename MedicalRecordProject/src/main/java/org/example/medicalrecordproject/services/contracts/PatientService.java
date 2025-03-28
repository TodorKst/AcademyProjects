package org.example.medicalrecordproject.services.contracts;

import org.example.medicalrecordproject.models.users.Patient;

import java.util.List;

public interface PatientService {
    List<Patient> getAllPatients();

    Patient getPatientById(long id);

    Patient savePatient(Patient patient);

    void deletePatient(long id);

    void updateAdmin(long id, Patient patient);

    void payInsurance(long id);
}
