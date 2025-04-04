package integration;

import org.example.medicalrecordproject.dtos.in.creation.DiagnosisCreationDto;
import org.example.medicalrecordproject.dtos.in.creation.MedicalVisitCreationDto;
import org.example.medicalrecordproject.dtos.out.response.MedicalVisitResponseDto;
import org.example.medicalrecordproject.exceptions.EntityNotFoundException;
import org.example.medicalrecordproject.exceptions.InsurancePaymentExpiredException;
import org.example.medicalrecordproject.exceptions.InvalidDiagnosisReferenceException;
import org.example.medicalrecordproject.exceptions.InvalidMedicalVisitException;
import org.example.medicalrecordproject.helpers.ValidationHelper;
import org.example.medicalrecordproject.helpers.mappers.EntityMapper;
import org.example.medicalrecordproject.models.Diagnosis;
import org.example.medicalrecordproject.models.MedicalVisit;
import org.example.medicalrecordproject.models.Specialty;
import org.example.medicalrecordproject.models.users.Doctor;
import org.example.medicalrecordproject.models.users.Patient;
import org.example.medicalrecordproject.repositories.MedicalVisitRepository;
import org.example.medicalrecordproject.services.MedicalVisitServiceImpl;
import org.example.medicalrecordproject.services.contracts.DiagnosisService;
import org.example.medicalrecordproject.services.contracts.DoctorService;
import org.example.medicalrecordproject.services.contracts.PatientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

// I know these don't cover the criteria for an integration test of the service but I wanted to have a batch of tests to
// check the validations not mocking them.
class MedicalVisitServiceTests {

    private MedicalVisitServiceImpl service;
    private MedicalVisitRepository repository;
    private ValidationHelper validator;
    private EntityMapper mapper;
    private DoctorService doctorService;
    private PatientService patientService;
    private DiagnosisService diagnosisService;

    @BeforeEach
    void setUp() {
        repository = mock(MedicalVisitRepository.class);
        validator = new ValidationHelper();
        mapper = mock(EntityMapper.class);
        doctorService = mock(DoctorService.class);
        patientService = mock(PatientService.class);
        diagnosisService = mock(DiagnosisService.class);

        service = new MedicalVisitServiceImpl(repository, validator, mapper, doctorService, patientService, diagnosisService);
    }

    @Test
    void SaveMedicalVisit_Should_Save_WhenValid() {
        MedicalVisit visit = getValidMedicalVisit();
        when(repository.save(visit)).thenReturn(visit);
        assertEquals(visit, service.saveMedicalVisit(visit));
    }

    @Test
    void SaveMedicalVisit_Should_Throw_WhenMissingDoctor() {
        MedicalVisit visit = getValidMedicalVisit();
        visit.setDoctor(null);
        assertThrows(InvalidMedicalVisitException.class, () -> service.saveMedicalVisit(visit));
    }

    @Test
    void SaveMedicalVisit_Should_Throw_WhenMissingPatient() {
        MedicalVisit visit = getValidMedicalVisit();
        visit.setPatient(null);
        assertThrows(InvalidMedicalVisitException.class, () -> service.saveMedicalVisit(visit));
    }

    @Test
    void SaveMedicalVisit_Should_Throw_WhenVisitInFuture() {
        MedicalVisit visit = getValidMedicalVisit();
        visit.setVisitDate(LocalDateTime.now().plusDays(1));
        assertThrows(InvalidMedicalVisitException.class, () -> service.saveMedicalVisit(visit));
    }

    @Test
    void SaveMedicalVisit_Should_Throw_WhenDiagnosisHasNullId() {
        MedicalVisit visit = getValidMedicalVisit();
        Diagnosis invalidDiagnosis = Diagnosis.builder().build();
        invalidDiagnosis.setId(null);
        visit.setDiagnoses(Set.of(invalidDiagnosis));
        assertThrows(InvalidDiagnosisReferenceException.class, () -> service.saveMedicalVisit(visit));
    }

    @Test
    void SaveMedicalVisit_Should_Throw_WhenInsurancePaymentMissing() {
        MedicalVisit visit = getValidMedicalVisit();
        visit.getPatient().setLastInsurancePayment(null);
        assertThrows(InsurancePaymentExpiredException.class, () -> service.saveMedicalVisit(visit));
    }

    @Test
    void SaveMedicalVisit_Should_Throw_WhenInsurancePaymentOld() {
        MedicalVisit visit = getValidMedicalVisit();
        visit.getPatient().setLastInsurancePayment(Date.valueOf(LocalDateTime.now().minusMonths(7).toLocalDate()));
        assertThrows(InsurancePaymentExpiredException.class, () -> service.saveMedicalVisit(visit));
    }

    @Test
    void CreateMedicalVisit_Should_Create_WhenValid() {
        MedicalVisitCreationDto dto = new MedicalVisitCreationDto();
        dto.setDoctorId(1L);
        dto.setPatientId(2L);
        dto.setVisitDate(LocalDateTime.now());
        dto.setTreatment("treatment");

        MedicalVisit mappedVisit = getValidMedicalVisit();
        when(mapper.toMedicalVisit(dto)).thenReturn(mappedVisit);
        when(doctorService.getDoctorById(1L)).thenReturn(mappedVisit.getDoctor());
        when(patientService.getPatientById(2L)).thenReturn(mappedVisit.getPatient());
        when(repository.save(mappedVisit)).thenReturn(mappedVisit);
        when(mapper.toMedicalVisitDto(mappedVisit)).thenReturn(new MedicalVisitResponseDto());

        assertNotNull(service.createMedicalVisit(dto));
    }

    @Test
    void UpdateMedicalVisit_Should_Throw_WhenNotFound() {
        when(repository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> service.updateMedicalVisit(99L, new MedicalVisitCreationDto()));
    }

    @Test
    void DeleteMedicalVisit_Should_CallRepository() {
        service.deleteMedicalVisit(88L);
        verify(repository).deleteById(88L);
    }

    @Test
    void GetMedicalVisitById_Should_Throw_WhenNotFound() {
        when(repository.findById(77L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> service.getMedicalVisitById(77L));
    }

    @Test
    void GetMedicalVisitByIdResponse_Should_ReturnDto() throws EntityNotFoundException {
        MedicalVisit visit = getValidMedicalVisit();
        when(repository.findById(1L)).thenReturn(Optional.of(visit));
        when(mapper.toMedicalVisitDto(visit)).thenReturn(new MedicalVisitResponseDto());
        assertNotNull(service.getMedicalVisitByIdResponse(1L));
    }

    @Test
    void GetByPatientId_Should_ReturnDtos() {
        List<MedicalVisit> visits = List.of(getValidMedicalVisit());
        when(repository.findByPatientId(1L)).thenReturn(visits);
        when(mapper.toMedicalVisitDtoList(visits)).thenReturn(List.of(new MedicalVisitResponseDto()));
        assertEquals(1, service.getByPatientId(1L).size());
    }

    @Test
    void GetByDoctorId_Should_ReturnDtos() {
        List<MedicalVisit> visits = List.of(getValidMedicalVisit());
        when(repository.findByDoctorId(1L)).thenReturn(visits);
        when(mapper.toMedicalVisitDtoList(visits)).thenReturn(List.of(new MedicalVisitResponseDto()));
        assertEquals(1, service.getByDoctorId(1L).size());
    }

    @Test
    void GetByVisitDate_Should_ReturnDtos() {
        String date = Instant.now().toString();
        List<MedicalVisit> visits = List.of(getValidMedicalVisit());
        when(repository.findByVisitDate(any())).thenReturn(visits);
        when(mapper.toMedicalVisitDtoList(visits)).thenReturn(List.of(new MedicalVisitResponseDto()));
        assertEquals(1, service.getByVisitDate(date).size());
    }

    @Test
    void GetByVisitDateBetween_Should_ReturnDtos() {
        LocalDateTime start = LocalDateTime.now().minusDays(2);
        LocalDateTime end = LocalDateTime.now().plusDays(2);
        List<MedicalVisit> visits = List.of(getValidMedicalVisit());
        when(repository.findByVisitDateBetween(start, end)).thenReturn(visits);
        when(mapper.toMedicalVisitDtoList(visits)).thenReturn(List.of(new MedicalVisitResponseDto()));
        assertEquals(1, service.getByVisitDateBetween(start, end).size());
    }

    @Test
    void GetByDateRangeAndDoctor_Should_ReturnDtos() {
        LocalDateTime start = LocalDateTime.now().minusDays(3);
        LocalDateTime end = LocalDateTime.now();
        List<MedicalVisit> visits = List.of(getValidMedicalVisit());
        when(repository.findByDateRangeAndOptionalDoctor(start, end, 1L)).thenReturn(visits);
        when(mapper.toMedicalVisitDtoList(visits)).thenReturn(List.of(new MedicalVisitResponseDto()));
        assertEquals(1, service.getByDateRangeAndDoctor(start, end, 1L).size());
    }

    @Test
    void AddDiagnosis_Should_AddAndReturnDto() throws EntityNotFoundException {
        MedicalVisit visit = getValidMedicalVisit();
        visit.setDiagnoses(new HashSet<>()); // 🔧 fix: use mutable set

        DiagnosisCreationDto dto = new DiagnosisCreationDto();
        Diagnosis newDiagnosis = Diagnosis.builder().build();

        when(repository.findById(1L)).thenReturn(Optional.of(visit));
        when(mapper.toDiagnosis(dto)).thenReturn(newDiagnosis);
        when(repository.save(visit)).thenReturn(visit);
        when(mapper.toMedicalVisitDto(visit)).thenReturn(new MedicalVisitResponseDto());

        assertNotNull(service.addDiagnosis(1L, dto));
        verify(diagnosisService).saveDiagnosis(newDiagnosis);
    }


    private MedicalVisit getValidMedicalVisit() {
        Doctor doctor = Doctor.builder()
                .isGp(true)
                .specialties(Set.of(Specialty.builder().name("Cardiology").build()))
                .build();

        Patient patient = Patient.builder()
                .gp(doctor)
                .lastInsurancePayment(Date.valueOf(LocalDateTime.now().minusMonths(1).toLocalDate()))
                .build();

        Diagnosis diagnosis = Diagnosis.builder().build();
        diagnosis.setId(1L);

        return MedicalVisit.builder()
                .doctor(doctor)
                .patient(patient)
                .visitDate(LocalDateTime.now())
                .diagnoses(Set.of(diagnosis))
                .treatment("treatment")
                .build();
    }

    @Test
    void UpdateMedicalVisit_Should_UpdateAndReturnDto_WhenFound() throws EntityNotFoundException {
        MedicalVisit existing = getValidMedicalVisit();

        MedicalVisitCreationDto dto = new MedicalVisitCreationDto();
        dto.setDoctorId(1L);
        dto.setPatientId(2L);
        dto.setVisitDate(LocalDateTime.now());
        dto.setTreatment("Updated treatment");

        Doctor doctor = existing.getDoctor();
        Patient patient = existing.getPatient();

        when(repository.findById(5L)).thenReturn(Optional.of(existing));
        when(doctorService.getDoctorById(1L)).thenReturn(doctor);
        when(patientService.getPatientById(2L)).thenReturn(patient);
        when(repository.save(existing)).thenReturn(existing);
        when(mapper.toMedicalVisitDto(existing)).thenReturn(new MedicalVisitResponseDto());

        MedicalVisitResponseDto result = service.updateMedicalVisit(5L, dto);

        assertNotNull(result);
        verify(repository).save(existing);
        verify(mapper).toMedicalVisitDto(existing);
    }
}