package org.example.medicalrecordproject.unit;

import org.example.medicalrecordproject.dtos.in.creation.DoctorCreationDto;
import org.example.medicalrecordproject.dtos.out.DoctorOutDto;
import org.example.medicalrecordproject.dtos.out.DoctorStatOutDto;
import org.example.medicalrecordproject.dtos.out.response.DoctorResponseDto;
import org.example.medicalrecordproject.exceptions.EntityNotFoundException;
import org.example.medicalrecordproject.helpers.ValidationHelper;
import org.example.medicalrecordproject.helpers.mappers.DoctorMapper;
import org.example.medicalrecordproject.helpers.mappers.RegisterMapper;
import org.example.medicalrecordproject.models.Specialty;
import org.example.medicalrecordproject.models.users.Doctor;
import org.example.medicalrecordproject.repositories.DoctorRepository;
import org.example.medicalrecordproject.repositories.MedicalVisitRepository;
import org.example.medicalrecordproject.services.DoctorServiceImpl;
import org.example.medicalrecordproject.services.contracts.SpecialtyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.sql.Timestamp;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DoctorServiceTests {

    @Mock private DoctorRepository doctorRepo;
    @Mock private MedicalVisitRepository visitRepo;
    @Mock private DoctorMapper doctorMapper;
    @Mock private RegisterMapper registerMapper;
    @Mock private SpecialtyService specialtyService;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private ValidationHelper validator;

    @InjectMocks
    private DoctorServiceImpl doctorService;

    @Mock private Doctor doctor;
    @Mock private DoctorResponseDto responseDto;
    @Mock private DoctorCreationDto creationDto;
    @Mock private Specialty specialty;
    @Mock private DoctorOutDto outDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllDoctors_Should_ReturnDtoList() {
        List<Doctor> doctors = List.of(doctor);
        List<DoctorResponseDto> dtos = List.of(responseDto);
        when(doctorRepo.findAll()).thenReturn(doctors);
        when(registerMapper.toDoctorDtoList(doctors)).thenReturn(dtos);
        assertEquals(dtos, doctorService.getAllDoctors());
    }

    @Test
    void getDoctorById_Should_ReturnDoctor_WhenExists() throws EntityNotFoundException {
        when(doctorRepo.findById(1L)).thenReturn(Optional.of(doctor));
        assertEquals(doctor, doctorService.getDoctorById(1L));
    }

    @Test
    void getDoctorById_Should_Throw_WhenMissing() {
        when(doctorRepo.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> doctorService.getDoctorById(1L));
    }

    @Test
    void getDoctorByIdResponse_Should_ReturnDto() throws EntityNotFoundException {
        when(doctorRepo.findById(1L)).thenReturn(Optional.of(doctor));
        when(registerMapper.toDoctorDto(doctor)).thenReturn(responseDto);
        assertEquals(responseDto, doctorService.getDoctorByIdResponse(1L));
    }

    @Test
    void saveDoctor_Should_ValidateAndSave() {
        when(doctor.getUsername()).thenReturn("username");
        when(doctorRepo.existsByUsername("username")).thenReturn(false);
        when(doctorRepo.save(doctor)).thenReturn(doctor);
        Doctor result = doctorService.saveDoctor(doctor);
        verify(validator).validateDoctorCreationData(doctor, false);
        assertEquals(doctor, result);
    }

    @Test
    void createDoctor_Should_MapSetEncodeSaveReturnDto() {
        Set<Long> specIds = Set.of(1L);
        Set<Specialty> specialties = Set.of(specialty);
        when(creationDto.getSpecialties()).thenReturn(specIds);
        when(specialtyService.getSpecialtyById(1L)).thenReturn(specialty);
        when(registerMapper.toDoctor(creationDto)).thenReturn(doctor);
        when(doctor.getPassword()).thenReturn("raw");
        when(passwordEncoder.encode("raw")).thenReturn("hashed");
        when(doctorRepo.existsByUsername(any())).thenReturn(false);
        when(doctorRepo.save(any())).thenReturn(doctor);
        when(registerMapper.toDoctorDto(doctor)).thenReturn(responseDto);

        DoctorResponseDto result = doctorService.createDoctor(creationDto, new Timestamp(System.currentTimeMillis()));

        verify(doctor).setCreatedAt(any());
        verify(doctor).setPassword("hashed");
        verify(doctor).setSpecialties(specialties);
        assertEquals(responseDto, result);
    }

    @Test
    void deleteDoctor_Should_CallDelete() {
        doctorService.deleteDoctor(1L);
        verify(doctorRepo).deleteById(1L);
    }

    @Test
    void updateDoctor_Should_ValidateAndSave() throws EntityNotFoundException {
        when(doctorRepo.findById(1L)).thenReturn(Optional.of(doctor));
        when(doctor.getUsername()).thenReturn("new");
        when(doctor.getPassword()).thenReturn("pass");
        when(doctorRepo.existsByUsername("new")).thenReturn(true);

        doctorService.updateDoctor(1L, doctor);

        verify(validator).validateUsernameChange("new", "new", true);
        verify(doctorRepo).save(any());
    }

    @Test
    void getAllWithSpeciality_Should_ReturnDtos() {
        List<Doctor> doctors = List.of(doctor);
        List<DoctorOutDto> dtos = List.of(outDto);
        when(specialtyService.getSpecialtyByName("Cardiology")).thenReturn(specialty);
        when(doctorRepo.findAllBySpecialtiesContains(specialty)).thenReturn(doctors);
        when(doctorMapper.toDtoList(doctors)).thenReturn(dtos);
        assertEquals(dtos, doctorService.getAllWithSpeciality("Cardiology"));
    }

    @Test
    void getAllGps_Should_ReturnDtos() {
        List<Doctor> doctors = List.of(doctor);
        List<DoctorOutDto> dtos = List.of(outDto);
        when(doctorRepo.findAllByIsGp(true)).thenReturn(doctors);
        when(doctorMapper.toDtoList(doctors)).thenReturn(dtos);
        assertEquals(dtos, doctorService.getAllGps());
    }

    @Test
    void countVisitsPerDoctor_Should_ReturnStats() {
        Long doctorId = 1L;
        Long visitCount = 5L;
        String doctorName = "Dr. A";

        List<Object[]> rows = new ArrayList<>();
        rows.add(new Object[] { doctorId, visitCount });

        when(visitRepo.countVisitsPerDoctor()).thenReturn(rows);
        when(doctorRepo.findById(doctorId)).thenReturn(Optional.of(doctor));
        when(doctor.getName()).thenReturn(doctorName);

        List<DoctorStatOutDto> stats = doctorService.countVisitsPerDoctor();

        assertEquals(1, stats.size());
        DoctorStatOutDto stat = stats.get(0);
        assertEquals(doctorId, stat.getDoctorId());
        assertEquals(doctorName, stat.getDoctorName());
        assertEquals(visitCount, stat.getCount());
    }



    @Test
    void getDoctorsWithMostSickLeaves_Should_ReturnDtos() {
        List<Doctor> doctors = List.of(doctor);
        List<DoctorOutDto> dtos = List.of(outDto);
        when(doctorRepo.findDoctorsWithMostSickLeaves()).thenReturn(doctors);
        when(doctorMapper.toDto(doctor)).thenReturn(outDto);
        assertEquals(dtos, doctorService.getDoctorsWithMostSickLeaves());
    }
}