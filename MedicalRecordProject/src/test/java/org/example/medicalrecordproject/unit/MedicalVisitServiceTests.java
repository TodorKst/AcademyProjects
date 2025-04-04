package org.example.medicalrecordproject.unit;

import org.example.medicalrecordproject.dtos.in.creation.DiagnosisCreationDto;
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
import org.example.medicalrecordproject.services.MedicalVisitServiceImpl;
import org.example.medicalrecordproject.services.contracts.DiagnosisService;
import org.example.medicalrecordproject.services.contracts.DoctorService;
import org.example.medicalrecordproject.services.contracts.PatientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

// Unit tests covering all cases for the service. There is another batch of
// test that doesnt mock the validators and check all validation cases
class MedicalVisitServiceTests {

    @Mock
    private MedicalVisitRepository repository;
    @Mock
    private ValidationHelper validator;
    @Mock
    private EntityMapper mapper;
    @Mock
    private DoctorService doctorService;
    @Mock
    private PatientService patientService;
    @Mock
    private DiagnosisService diagnosisService;

    @InjectMocks
    private MedicalVisitServiceImpl service;

    @Mock
    private MedicalVisit visit;
    @Mock
    private Doctor doctor;
    @Mock
    private Patient patient;
    @Mock
    private Diagnosis diagnosis;
    @Mock
    private MedicalVisitCreationDto creationDto;
    @Mock
    private MedicalVisitResponseDto responseDto;
    @Mock
    private DiagnosisCreationDto diagnosisDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        service = spy(new MedicalVisitServiceImpl(
                repository, validator, mapper, doctorService, patientService, diagnosisService
        ));
    }

    @Test
    void getAllMedicalVisits_Should_ReturnDtos() {
        List<MedicalVisit> visits = List.of(visit);
        List<MedicalVisitResponseDto> dtos = List.of(responseDto);
        when(repository.findAll()).thenReturn(visits);
        when(mapper.toMedicalVisitDtoList(visits)).thenReturn(dtos);
        assertEquals(dtos, service.getAllMedicalVisits());
    }

    @Test
    void getMedicalVisitById_Should_ReturnVisit_WhenFound() throws EntityNotFoundException {
        when(repository.findById(1L)).thenReturn(Optional.of(visit));
        assertEquals(visit, service.getMedicalVisitById(1L));
    }

    @Test
    void getMedicalVisitById_Should_Throw_WhenNotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> service.getMedicalVisitById(1L));
    }

    @Test
    void getMedicalVisitByIdResponse_Should_ReturnDto() throws EntityNotFoundException {
        when(repository.findById(1L)).thenReturn(Optional.of(visit));
        when(mapper.toMedicalVisitDto(visit)).thenReturn(responseDto);
        assertEquals(responseDto, service.getMedicalVisitByIdResponse(1L));
    }

    @Test
    void saveMedicalVisit_Should_ValidateAndSave() {
        when(repository.save(visit)).thenReturn(visit);
        MedicalVisit result = service.saveMedicalVisit(visit);
        verify(validator).validateMedicalVisitCreationData(visit);
        assertEquals(visit, result);
    }

    @Test
    void createMedicalVisit_Should_MapValidateSaveReturnDto() {
        when(mapper.toMedicalVisit(creationDto)).thenReturn(visit);
        when(creationDto.getDoctorId()).thenReturn(1L);
        when(creationDto.getPatientId()).thenReturn(2L);
        when(doctorService.getDoctorById(1L)).thenReturn(doctor);
        when(patientService.getPatientById(2L)).thenReturn(patient);
        when(repository.save(visit)).thenReturn(visit);
        when(mapper.toMedicalVisitDto(visit)).thenReturn(responseDto);

        MedicalVisitResponseDto result = service.createMedicalVisit(creationDto);

        verify(validator).validateMedicalVisitCreationData(visit);
        assertEquals(responseDto, result);
    }

    @Test
    void deleteMedicalVisit_Should_CallDelete() {
        service.deleteMedicalVisit(10L);
        verify(repository).deleteById(10L);
    }

    @Test
    void updateMedicalVisit_Should_SetFieldsAndSave() throws EntityNotFoundException {
        when(repository.findById(1L)).thenReturn(Optional.of(visit));
        when(creationDto.getDoctorId()).thenReturn(1L);
        when(creationDto.getPatientId()).thenReturn(2L);
        when(doctorService.getDoctorById(1L)).thenReturn(doctor);
        when(patientService.getPatientById(2L)).thenReturn(patient);
        when(creationDto.getVisitDate()).thenReturn(LocalDateTime.now());
        when(creationDto.getTreatment()).thenReturn("treatment");

        service.updateMedicalVisit(1L, creationDto);

        verify(visit).setDoctor(doctor);
        verify(visit).setPatient(patient);
        verify(visit).setVisitDate(any());
        verify(visit).setTreatment("treatment");
        verify(repository).save(visit);
    }

    @Test
    void getByPatientId_Should_ReturnDtos() {
        List<MedicalVisit> visits = List.of(visit);
        when(repository.findByPatientId(1L)).thenReturn(visits);
        when(mapper.toMedicalVisitDtoList(visits)).thenReturn(List.of(responseDto));
        assertEquals(1, service.getByPatientId(1L).size());
    }

    @Test
    void getByDoctorId_Should_ReturnDtos() {
        List<MedicalVisit> visits = List.of(visit);
        when(repository.findByDoctorId(1L)).thenReturn(visits);
        when(mapper.toMedicalVisitDtoList(visits)).thenReturn(List.of(responseDto));
        assertEquals(1, service.getByDoctorId(1L).size());
    }

    @Test
    void getByVisitDate_Should_ReturnDtos() {
        LocalDateTime date = LocalDateTime.now();
        String dateStr = date.toInstant(java.time.ZoneOffset.UTC).toString();
        when(repository.findByVisitDate(any())).thenReturn(List.of(visit));
        when(mapper.toMedicalVisitDtoList(any())).thenReturn(List.of(responseDto));
        assertEquals(1, service.getByVisitDate(dateStr).size());
    }

    @Test
    void getByVisitDateBetween_Should_ReturnDtos() {
        LocalDateTime start = LocalDateTime.now().minusDays(1);
        LocalDateTime end = LocalDateTime.now().plusDays(1);
        when(repository.findByVisitDateBetween(start, end)).thenReturn(List.of(visit));
        when(mapper.toMedicalVisitDtoList(any())).thenReturn(List.of(responseDto));
        assertEquals(1, service.getByVisitDateBetween(start, end).size());
    }

    @Test
    void getByDateRangeAndDoctor_Should_ReturnDtos() {
        LocalDateTime start = LocalDateTime.now().minusDays(1);
        LocalDateTime end = LocalDateTime.now().plusDays(1);
        when(repository.findByDateRangeAndOptionalDoctor(start, end, 1L)).thenReturn(List.of(visit));
        when(mapper.toMedicalVisitDtoList(any())).thenReturn(List.of(responseDto));
        assertEquals(1, service.getByDateRangeAndDoctor(start, end, 1L).size());
    }

    @Test
    void addDiagnosis_Should_AddToVisitAndReturnDto() throws EntityNotFoundException {
        Set<Diagnosis> diagnoses = new HashSet<>();
        doReturn(visit).when(service).getMedicalVisitById(1L);
        when(visit.getDiagnoses()).thenReturn(diagnoses);
        when(mapper.toDiagnosis(diagnosisDto)).thenReturn(diagnosis);
        when(repository.save(visit)).thenReturn(visit);
        when(mapper.toMedicalVisitDto(visit)).thenReturn(responseDto);

        MedicalVisitResponseDto result = service.addDiagnosis(1L, diagnosisDto);

        verify(diagnosis).setMedicalVisits(Set.of(visit));
        verify(visit).setDiagnoses(any());
        verify(diagnosisService).saveDiagnosis(diagnosis);
        assertEquals(responseDto, result);
    }

}
