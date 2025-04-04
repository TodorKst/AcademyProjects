package org.example.medicalrecordproject.unit;

import org.example.medicalrecordproject.dtos.in.creation.PatientCreationDto;
import org.example.medicalrecordproject.dtos.out.GpPatientCountOutDto;
import org.example.medicalrecordproject.dtos.out.response.PatientResponseDto;
import org.example.medicalrecordproject.exceptions.EntityNotFoundException;
import org.example.medicalrecordproject.helpers.ValidationHelper;
import org.example.medicalrecordproject.helpers.mappers.RegisterMapper;
import org.example.medicalrecordproject.models.users.Doctor;
import org.example.medicalrecordproject.models.users.Patient;
import org.example.medicalrecordproject.repositories.PatientRepository;
import org.example.medicalrecordproject.services.PatientServiceImpl;
import org.example.medicalrecordproject.services.contracts.DoctorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PatientServiceTests {

    @Mock private PatientRepository repository;
    @Mock private RegisterMapper mapper;
    @Mock private DoctorService doctorService;
    @Mock private PasswordEncoder encoder;
    @Mock private ValidationHelper validator;

    @InjectMocks private PatientServiceImpl service;

    @Mock private Patient patient;
    @Mock private Doctor doctor;
    @Mock private PatientResponseDto responseDto;
    @Mock private PatientCreationDto creationDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllPatients_Should_ReturnDtos() {
        List<Patient> patients = List.of(patient);
        List<PatientResponseDto> dtos = List.of(responseDto);
        when(repository.findAll()).thenReturn(patients);
        when(mapper.toPatientDtoList(patients)).thenReturn(dtos);
        assertEquals(dtos, service.getAllPatients());
    }

    @Test
    void getPatientById_Should_ReturnPatient_WhenFound() throws EntityNotFoundException {
        when(repository.findById(1L)).thenReturn(Optional.of(patient));
        assertEquals(patient, service.getPatientById(1L));
    }

    @Test
    void getPatientById_Should_Throw_WhenNotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> service.getPatientById(1L));
    }

    @Test
    void getPatientByIdResponse_Should_ReturnDto() throws EntityNotFoundException {
        when(repository.findById(1L)).thenReturn(Optional.of(patient));
        when(mapper.toPatientDto(patient)).thenReturn(responseDto);
        assertEquals(responseDto, service.getPatientByIdResponse(1L));
    }

    @Test
    void savePatient_Should_Validate_And_Save() {
        when(patient.getUsername()).thenReturn("username");
        when(repository.existsByUsername("username")).thenReturn(false);
        when(repository.save(patient)).thenReturn(patient);
        when(mapper.toPatientDto(patient)).thenReturn(responseDto);

        PatientResponseDto result = service.savePatient(patient);

        verify(validator).validatePatientCreationData(patient, false);
        assertEquals(responseDto, result);
    }

    @Test
    void createPatient_Should_MapValidateEncodeSaveAndReturnDto() {
        Timestamp timestamp = Timestamp.valueOf(LocalDate.now().atStartOfDay());

        when(mapper.toPatient(creationDto)).thenReturn(patient);
        when(creationDto.getGpId()).thenReturn(1L);
        when(doctorService.getDoctorById(1L)).thenReturn(doctor);
        when(patient.getPassword()).thenReturn("plainPass");
        when(encoder.encode("plainPass")).thenReturn("encodedPass");
        when(patient.getUsername()).thenReturn("username");
        when(repository.existsByUsername("username")).thenReturn(false);
        when(repository.save(patient)).thenReturn(patient);
        when(mapper.toPatientDto(patient)).thenReturn(responseDto);

        PatientResponseDto result = service.createPatient(creationDto, timestamp);

        verify(patient).setGp(doctor);
        verify(patient).setCreatedAt(timestamp);
        verify(patient).setPassword("encodedPass");
        verify(patient).setId(null);
        verify(validator).validatePatientCreationData(patient, false);
        assertEquals(responseDto, result);
    }

    @Test
    void deletePatient_Should_CallRepoDelete() {
        service.deletePatient(1L);
        verify(repository).deleteById(1L);
    }

    @Test
    void updatePatient_Should_SetFieldsAndValidateAndSave() throws EntityNotFoundException {
        when(repository.findById(1L)).thenReturn(Optional.of(patient));
        when(creationDto.getName()).thenReturn("New Name");
        when(creationDto.getUsername()).thenReturn("newUsername");
        when(creationDto.getPassword()).thenReturn("newPass");
        when(creationDto.getGpId()).thenReturn(3L);
        when(patient.getUsername()).thenReturn("oldUsername");
        when(repository.existsByUsername("newUsername")).thenReturn(false);
        when(doctorService.getDoctorById(3L)).thenReturn(doctor);
        when(encoder.encode("newPass")).thenReturn("encodedPass");

        service.updatePatient(1L, creationDto);

        verify(validator).validateUsernameChange("newUsername", "oldUsername", false);
        verify(patient).setName("New Name");
        verify(patient).setUsername("newUsername");
        verify(patient).setPassword("encodedPass");
        verify(patient).setGp(doctor);
        verify(validator).validatePatientUpdateData(patient);
        verify(repository).save(patient);
    }

    @Test
    void payInsurance_Should_UpdateDateAndSave() {
        when(repository.findById(1L)).thenReturn(Optional.of(patient));

        service.payInsurance(1L);

        verify(patient).setLastInsurancePayment(any(Date.class));
        verify(repository).save(patient);
    }

    @Test
    void getPatientsByDiagnosis_Should_ReturnDtos() {
        List<Patient> patients = List.of(patient);
        when(repository.findByDiagnosisName("flu")).thenReturn(patients);
        when(mapper.toPatientDtoList(patients)).thenReturn(List.of(responseDto));
        assertEquals(1, service.getPatientsByDiagnosis("flu").size());
    }

    @Test
    void getPatientsByGp_Should_ReturnDtos() {
        List<Patient> patients = List.of(patient);
        when(repository.findByGpId(1L)).thenReturn(patients);
        when(mapper.toPatientDtoList(patients)).thenReturn(List.of(responseDto));
        assertEquals(1, service.getPatientsByGp(1L).size());
    }

    @Test
    void countPatientsPerGp_Should_MapToDtoList() {
        Long gpId = 1L;
        Long patientCount = 5L;
        Object[] row = new Object[]{gpId, patientCount};
        when(repository.countPatientsByGp()).thenReturn(List.<Object[]>of(row));

        List<GpPatientCountOutDto> result = service.countPatientsPerGp();
        
        assertNotNull(result);
        assertEquals(1, result.size());
        GpPatientCountOutDto dto = result.get(0);
        assertEquals(gpId, dto.getGpId());
        assertEquals(patientCount, dto.getPatientCount());
    }

}
