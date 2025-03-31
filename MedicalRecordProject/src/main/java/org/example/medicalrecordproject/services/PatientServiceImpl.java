package org.example.medicalrecordproject.services;

import org.example.medicalrecordproject.dtos.out.GpPatientCountOutDto;
import org.example.medicalrecordproject.dtos.out.PatientOutDto;
import org.example.medicalrecordproject.exceptions.EntityNotFoundException;
import org.example.medicalrecordproject.helpers.ValidationHelper;
import org.example.medicalrecordproject.models.users.Patient;
import org.example.medicalrecordproject.repositories.PatientRepository;
import org.example.medicalrecordproject.services.contracts.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

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
    public Patient getPatientById(long id) throws EntityNotFoundException {
        return patientRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Patient"));
    }

    @Override
    public Patient savePatient(Patient patient) {
        ValidationHelper.validateAssignedGp(patient);
        ValidationHelper.checkUsernameUniqueness(patientRepository.findByUsername(patient.getUsername()));
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
        ValidationHelper.validateAssignedGp(patient);
        ValidationHelper.validateUsernameChange(existingPatient.getUsername(), patient.getUsername(),
                patientRepository.findByUsername(patient.getUsername()));
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
        existingPatient.setLastInsurancePayment(Date.valueOf(LocalDate.now()));
        patientRepository.save(existingPatient);
    }

    @Override
    public List<PatientOutDto> getPatientsByDiagnosis(String diagnosisName) {
        return patientRepository.findByDiagnosisName(diagnosisName)
                .stream()
                .map(p -> new PatientOutDto(p.getId(), p.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public List<PatientOutDto> getPatientsByGp(Long gpId) {
        return patientRepository.findByGpId(gpId)
                .stream()
                .map(p -> new PatientOutDto(p.getId(), p.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public List<GpPatientCountOutDto> countPatientsPerGp() {
        return patientRepository.countPatientsByGp()
                .stream()
                .map(row -> new GpPatientCountOutDto((Long) row[0], (Long) row[1]))
                .collect(Collectors.toList());
    }
}
