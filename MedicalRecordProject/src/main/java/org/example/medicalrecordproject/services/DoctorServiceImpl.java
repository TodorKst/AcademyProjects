package org.example.medicalrecordproject.services;

import jakarta.transaction.Transactional;
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
import org.example.medicalrecordproject.services.contracts.DoctorService;
import org.example.medicalrecordproject.services.contracts.SpecialtyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepository;
    private final MedicalVisitRepository medicalVisitRepository;
    private final DoctorMapper doctorMapper;
    private final RegisterMapper registerMapper;
    private final SpecialtyService specialtyService;
    private final PasswordEncoder passwordEncoder;
    private final ValidationHelper validationHelper;

    @Autowired
    public DoctorServiceImpl(DoctorRepository doctorRepository,
                             MedicalVisitRepository medicalVisitRepository,
                             DoctorMapper doctorMapper,
                             RegisterMapper registerMapper,
                             SpecialtyService specialtyService,
                             PasswordEncoder passwordEncoder,
                             ValidationHelper validationHelper) {
        this.doctorRepository = doctorRepository;
        this.medicalVisitRepository = medicalVisitRepository;
        this.doctorMapper = doctorMapper;
        this.registerMapper = registerMapper;
        this.specialtyService = specialtyService;
        this.passwordEncoder = passwordEncoder;
        this.validationHelper = validationHelper;
    }

    @Transactional
    @Override
    public List<DoctorResponseDto> getAllDoctors() {
        List<Doctor> doctors = doctorRepository.findAll();
        return registerMapper.toDoctorDtoList(doctors);
    }

    @Transactional
    @Override
    public Doctor getDoctorById(long id) throws EntityNotFoundException {
            return doctorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Doctor"));
    }

    @Override
    public DoctorResponseDto getDoctorByIdResponse(long id) throws EntityNotFoundException {
        return registerMapper.toDoctorDto(getDoctorById(id));
    }

    @Override
    public Doctor saveDoctor(Doctor doctor) {
        validationHelper.validateDoctorCreationData(doctor, doctorRepository.existsByUsername(doctor.getUsername()));

        return doctorRepository.save(doctor);
    }

    @Override
    public DoctorResponseDto createDoctor(org.example.medicalrecordproject.dtos.in.creation.DoctorCreationDto dto, Timestamp createdAt) {
        Doctor doctor = registerMapper.toDoctor(dto);

        doctor.setCreatedAt(createdAt);
        doctor.setPassword(passwordEncoder.encode(doctor.getPassword()));

        Set<Specialty> specialties = dto.getSpecialties().stream()
                .map(specialtyService::getSpecialtyById)
                .collect(Collectors.toSet());
        doctor.setSpecialties(specialties);

        saveDoctor(doctor);

        return registerMapper.toDoctorDto(doctor);
    }


    @Override
    public void deleteDoctor(long id) {
        doctorRepository.deleteById(id);
    }

    @Override
    public DoctorResponseDto updateDoctor(long id, Doctor doctor) throws EntityNotFoundException {
        Doctor existingDoctor = doctorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Doctor"));

        validationHelper.validateUsernameChange(doctor.getUsername(), existingDoctor.getUsername(),
                doctorRepository.existsByUsername(doctor.getUsername()));

        existingDoctor.setSpecialties(doctor.getSpecialties());
        existingDoctor.setName(doctor.getName());
        existingDoctor.setUsername(doctor.getUsername());
        existingDoctor.setPassword(doctor.getPassword());
        existingDoctor.setIsGp(doctor.getIsGp());

        return registerMapper.toDoctorDto(doctorRepository.save(existingDoctor));
    }

    @Override
    public List<DoctorOutDto> getAllWithSpeciality(String specialty) {
        Specialty specialty1 = specialtyService.getSpecialtyByName(specialty);

        List<Doctor> doctors = doctorRepository.findAllBySpecialtiesContains(specialty1);
        return doctorMapper.toDtoList(doctors);
    }

    @Override
    public List<DoctorOutDto> getAllGps() {
        return doctorMapper.toDtoList(doctorRepository.findAllByIsGp(true));
    }

    @Override
    public List<DoctorStatOutDto> countVisitsPerDoctor() {
        return medicalVisitRepository.countVisitsPerDoctor()
                .stream()
                .map(row -> {
                    Long doctorId = (Long) row[0];
                    Long count = (Long) row[1];
                    String name = doctorRepository.findById(doctorId)
                            .orElseThrow(() -> new EntityNotFoundException("Doctor"))
                            .getName();
                    return new DoctorStatOutDto(doctorId, name, count);
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<DoctorOutDto> getDoctorsWithMostSickLeaves() {
        return doctorRepository.findDoctorsWithMostSickLeaves()
                .stream()
                .map(doctorMapper::toDto)
                .collect(Collectors.toList());
    }
}
