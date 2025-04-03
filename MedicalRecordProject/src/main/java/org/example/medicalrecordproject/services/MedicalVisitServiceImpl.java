package org.example.medicalrecordproject.services;

import org.example.medicalrecordproject.dtos.in.creation.MedicalVisitCreationDto;
import org.example.medicalrecordproject.dtos.out.response.MedicalVisitResponseDto;
import org.example.medicalrecordproject.exceptions.EntityNotFoundException;
import org.example.medicalrecordproject.helpers.ValidationHelper;
import org.example.medicalrecordproject.helpers.mappers.EntityMapper;
import org.example.medicalrecordproject.models.Diagnosis;
import org.example.medicalrecordproject.models.MedicalVisit;
import org.example.medicalrecordproject.models.users.Doctor;
import org.example.medicalrecordproject.models.users.Patient;
import org.example.medicalrecordproject.repositories.MedicalVisitRepository;
import org.example.medicalrecordproject.services.contracts.DiagnosisService;
import org.example.medicalrecordproject.services.contracts.DoctorService;
import org.example.medicalrecordproject.services.contracts.MedicalVisitService;
import org.example.medicalrecordproject.services.contracts.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.print.Doc;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MedicalVisitServiceImpl implements MedicalVisitService {

    private final MedicalVisitRepository medicalVisitRepository;
    private final ValidationHelper validationHelper;
    private final EntityMapper entityMapper;
    private final DoctorService doctorService;
    private final PatientService patientService;
    private final DiagnosisService diagnosisService;

    @Autowired
    public MedicalVisitServiceImpl(MedicalVisitRepository medicalVisitRepository,
                                   ValidationHelper validationHelper,
                                   EntityMapper entityMapper,
                                   DoctorService doctorService,
                                   PatientService patientService,
                                   DiagnosisService diagnosisService) {
        this.medicalVisitRepository = medicalVisitRepository;
        this.validationHelper = validationHelper;
        this.entityMapper = entityMapper;
        this.doctorService = doctorService;
        this.patientService = patientService;
        this.diagnosisService = diagnosisService;
    }

    @Override
    public List<MedicalVisit> getAllMedicalVisits() {
        return medicalVisitRepository.findAll();
    }

    @Override
    public MedicalVisit getMedicalVisitById(long id) throws EntityNotFoundException {
        return medicalVisitRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("MedicalVisit with id: " + id));
    }

    @Override
    public MedicalVisit saveMedicalVisit(MedicalVisit medicalVisit) {
        validationHelper.validateMedicalVisitCreationData(medicalVisit);

        return medicalVisitRepository.save(medicalVisit);
    }

    @Override
    public MedicalVisitResponseDto createMedicalVisit(MedicalVisitCreationDto dto) {
        MedicalVisit medicalVisit = entityMapper.toMedicalVisit(dto);
        medicalVisit.setDoctor(doctorService.getDoctorById(dto.getDoctorId()));
        medicalVisit.setPatient(patientService.getPatientById(dto.getPatientId()));

        validationHelper.validateMedicalVisitCreationData(medicalVisit);

        return entityMapper.toMedicalVisitDto(medicalVisitRepository.save(medicalVisit));
    }

    @Override
    public void deleteMedicalVisit(long id) {
        medicalVisitRepository.deleteById(id);
    }

    @Override
    public MedicalVisitResponseDto updateMedicalVisit(long id, MedicalVisitCreationDto dto) throws EntityNotFoundException {
        MedicalVisit existingMedicalVisit = medicalVisitRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("MedicalVisit with id: " + id));

        Doctor doctor = doctorService.getDoctorById(dto.getDoctorId());
        Patient patient = patientService.getPatientById(dto.getPatientId());

        existingMedicalVisit.setDoctor(doctor);
        existingMedicalVisit.setVisitDate(Timestamp.from(dto.getVisitDate().toInstant()).toLocalDateTime());
        existingMedicalVisit.setPatient(patient);
        existingMedicalVisit.setTreatment(dto.getTreatment());

        saveMedicalVisit(existingMedicalVisit);

        return entityMapper.toMedicalVisitDto(existingMedicalVisit);
    }

    @Override
    public List<MedicalVisitResponseDto> getByPatientId(long id) {
        return entityMapper.toMedicalVisitDtoList(medicalVisitRepository.findByPatientId(id));
    }

    @Override
    public List<MedicalVisitResponseDto> getByDoctorId(long id) {
        return entityMapper.toMedicalVisitDtoList(medicalVisitRepository.findByDoctorId(id));
    }

    @Override
    public List<MedicalVisitResponseDto> getByVisitDate(String date) {
        LocalDateTime dateTime = LocalDateTime.from(java.time.Instant.parse(date));

        return entityMapper.toMedicalVisitDtoList(medicalVisitRepository.findByVisitDate(dateTime));
    }

    @Override
    public List<MedicalVisitResponseDto> getByVisitDateBetween(LocalDateTime startDate, LocalDateTime endDate) {
        return entityMapper.toMedicalVisitDtoList(medicalVisitRepository.findByVisitDateBetween(startDate, endDate));
    }

    @Override
    public List<MedicalVisitResponseDto> getByDateRangeAndDoctor(LocalDateTime start, LocalDateTime end, Long doctorId) {
        return entityMapper.toMedicalVisitDtoList(medicalVisitRepository.findByDateRangeAndOptionalDoctor(start, end, doctorId));
    }
}
