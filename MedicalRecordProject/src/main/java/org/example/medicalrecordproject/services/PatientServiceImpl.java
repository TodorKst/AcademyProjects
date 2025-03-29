package org.example.medicalrecordproject.services;

import org.example.medicalrecordproject.exceptions.EntityNotFoundException;
import org.example.medicalrecordproject.models.users.Patient;
import org.example.medicalrecordproject.repositories.PatientRepository;
import org.example.medicalrecordproject.services.contracts.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@Service
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;

    @Autowired
    public PatientServiceImpl(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    @Override
    public Patient getPatientById(long id) throws EntityNotFoundException{
        return patientRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Patient"));
    }

    @Override
    public Patient savePatient(Patient patient) {
        return patientRepository.save(patient);
    }

    @Override
    public void deletePatient(long id) {
        patientRepository.deleteById(id);
    }

    @Override
    public void updatePatient(long id, Patient patient) throws EntityNotFoundException {
        Patient existingPatient = patientRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(patient.getName()));
            existingPatient.setName(patient.getName());
            existingPatient.setUsername(patient.getUsername());
            existingPatient.setPassword(patient.getPassword());
            existingPatient.setGp(patient.getGp());
            existingPatient.setLastInsurancePayment(patient.getLastInsurancePayment());
            patientRepository.save(existingPatient);
    }

    @Override
    public void payInsurance(long id) {
        Patient existingPatient = patientRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Patient"));
        if (existingPatient != null) {
            existingPatient.setLastInsurancePayment(Date.valueOf(LocalDate.now()));
            patientRepository.save(existingPatient);
        }
    }
}
