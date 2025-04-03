package org.example.medicalrecordproject.unit;

import org.example.medicalrecordproject.exceptions.EntityNotFoundException;
import org.example.medicalrecordproject.models.Diagnosis;
import org.example.medicalrecordproject.models.MedicalVisit;
import org.example.medicalrecordproject.models.users.Doctor;
import org.example.medicalrecordproject.models.users.Patient;
import org.example.medicalrecordproject.repositories.MedicalVisitRepository;
import org.example.medicalrecordproject.services.MedicalVisitServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAmount;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MedicalVisitServiceTests {

    @Mock
    private MedicalVisitRepository medicalVisitRepository;

    @InjectMocks
    private MedicalVisitServiceImpl medicalVisitService;

    @Mock
    private MedicalVisit mockVisit;

    @Mock
    private Patient mockPatient;

    @Mock
    private Doctor mockDoctor;

    private final LocalDateTime mockDateTime = LocalDateTime.now();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        when(mockVisit.getPatient()).thenReturn(mockPatient);
        when(mockVisit.getDoctor()).thenReturn(mockDoctor);
        when(mockVisit.getDiagnoses()).thenReturn(new HashSet<Diagnosis>());
        when(mockVisit.getVisitDate()).thenReturn(mockDateTime);
        when(mockVisit.getTreatment()).thenReturn("Sample treatment");
    }

    @Test
    void testGetMedicalVisitById_found() throws EntityNotFoundException {
        when(medicalVisitRepository.findById(1L)).thenReturn(Optional.of(mockVisit));
        MedicalVisit result = medicalVisitService.getMedicalVisitById(1L);
        assertEquals(mockVisit, result);
    }

    @Test
    void testGetMedicalVisitById_notFound() {
        when(medicalVisitRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> medicalVisitService.getMedicalVisitById(1L));
    }

    @Test
    void testUpdateMedicalVisit_success() {
        when(mockPatient.getName()).thenReturn("John Doe");
        when(mockPatient.getLastInsurancePayment()).thenReturn(new Date(Timestamp.from(Instant.now().minus(3, ChronoUnit.DAYS)).getTime()));
        when(medicalVisitRepository.findById(1L)).thenReturn(Optional.of(mockVisit));

        medicalVisitService.updateMedicalVisit(1L, mockVisit);

        verify(mockVisit, atLeastOnce()).getPatient();
        verify(mockVisit, atLeastOnce()).getDoctor();
        verify(mockVisit, atLeastOnce()).getDiagnoses();
        verify(mockVisit, atLeastOnce()).getVisitDate();
        verify(mockVisit, atLeastOnce()).getTreatment();

        verify(medicalVisitRepository).save(mockVisit);
    }

    @Test
    void testDeleteMedicalVisit() {
        medicalVisitService.deleteMedicalVisit(1L);
        verify(medicalVisitRepository).deleteById(1L);
    }

    @Test
    void testGetByPatientId() {
        when(medicalVisitRepository.findByPatientId(1L)).thenReturn(List.of(mockVisit));
        List<MedicalVisit> result = medicalVisitService.getByPatientId(1L);
        assertEquals(1, result.size());
    }

    @Test
    void testGetByDoctorId() {
        when(medicalVisitRepository.findByDoctorId(1L)).thenReturn(List.of(mockVisit));
        List<MedicalVisit> result = medicalVisitService.getByDoctorId(1L);
        assertEquals(1, result.size());
    }

    @Test
    void testGetByVisitDateBetween() {
        LocalDateTime start = LocalDateTime.now().minusDays(1);
        LocalDateTime end = LocalDateTime.now().plusDays(1);
        when(medicalVisitRepository.findByVisitDateBetween(start, end)).thenReturn(List.of(mockVisit));
        List<MedicalVisit> result = medicalVisitService.getByVisitDateBetween(start, end);
        assertEquals(1, result.size());
    }

    @Test
    void testGetByDateRangeAndDoctor() {
        LocalDateTime start = LocalDateTime.now().minusDays(1);
        LocalDateTime end = LocalDateTime.now().plusDays(1);
        when(medicalVisitRepository.findByDateRangeAndOptionalDoctor(start, end, 1L)).thenReturn(List.of(mockVisit));
        List<MedicalVisit> result = medicalVisitService.getByDateRangeAndDoctor(start, end, 1L);
        assertEquals(1, result.size());
    }
}
