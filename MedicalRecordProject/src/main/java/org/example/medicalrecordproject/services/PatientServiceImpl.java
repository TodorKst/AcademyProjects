package org.example.medicalrecordproject.services;

import org.example.medicalrecordproject.dtos.in.PatientCreationDto;
import org.example.medicalrecordproject.dtos.out.GpPatientCountOutDto;
import org.example.medicalrecordproject.dtos.out.PatientOutDto;
import org.example.medicalrecordproject.dtos.out.PatientCreationResponseDto;
import org.example.medicalrecordproject.exceptions.EntityNotFoundException;
import org.example.medicalrecordproject.helpers.RegisterMapper;
import org.example.medicalrecordproject.helpers.ValidationHelper;
import org.example.medicalrecordproject.models.users.Doctor;
import org.example.medicalrecordproject.models.users.Patient;
import org.example.medicalrecordproject.repositories.PatientRepository;
import org.example.medicalrecordproject.services.contracts.DoctorService;
import org.example.medicalrecordproject.services.contracts.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;
    private final RegisterMapper registerMapper;
    private final DoctorService doctorService;
    private final PasswordEncoder passwordEncoder;
    private final ValidationHelper validationHelper;

    @Autowired
    public PatientServiceImpl(PatientRepository patientRepository,
                              RegisterMapper registerMapper,
                              DoctorService doctorService,
                              PasswordEncoder passwordEncoder,
                              ValidationHelper validationHelper) {
        this.patientRepository = patientRepository;
        this.registerMapper = registerMapper;
        this.doctorService = doctorService;
        this.passwordEncoder = passwordEncoder;
        this.validationHelper = validationHelper;
    }

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
        validationHelper.validatePatientCreationData(patient, patientRepository.existsByUsername(patient.getUsername()));

        return patientRepository.save(patient);
    }

    @Override
    public PatientCreationResponseDto createPatient(PatientCreationDto patientDto, Timestamp createdAt) {
        Patient patient = registerMapper.toPatient(patientDto);
        Doctor gp = doctorService.getDoctorById(patientDto.getGpId());
        patient.setGp(gp);
        patient.setCreatedAt(createdAt);
        patient.setPassword(passwordEncoder.encode(patient.getPassword()));
        patient.setId(null); // guaranteed to avoid StaleStateException, there was a problem with the id
        savePatient(patient);

        return registerMapper.toPatientDto(patient);
    }

    @Override
    public void deletePatient(long id) {
        patientRepository.deleteById(id);
    }

    @Override
    public void updatePatient(long id, Patient patient) throws EntityNotFoundException {
        Patient existingPatient = patientRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(patient.getName()));

        validationHelper.validatePatientCreationData(patient, patientRepository.existsByUsername(patient.getUsername()));
        validationHelper.validateUsernameChange(patient.getUsername(), existingPatient.getUsername(), patientRepository.existsByUsername(patient.getUsername()));

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
