package org.example.medicalrecordproject.services;

import org.example.medicalrecordproject.exceptions.EntityNotFoundException;
import org.example.medicalrecordproject.helpers.ValidationHelper;
import org.example.medicalrecordproject.models.Specialty;
import org.example.medicalrecordproject.models.users.Doctor;
import org.example.medicalrecordproject.repositories.DoctorRepository;
import org.example.medicalrecordproject.repositories.MedicalVisitRepository;
import org.example.medicalrecordproject.services.contracts.DoctorService;
import org.example.medicalrecordproject.helpers.DoctorMapper;
import org.example.medicalrecordproject.dtos.out.DoctorOutDto;
import org.example.medicalrecordproject.dtos.out.DoctorStatOutDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepository;
    private final MedicalVisitRepository medicalVisitRepository;
    private final DoctorMapper doctorMapper;

    @Autowired
    public DoctorServiceImpl(DoctorRepository doctorRepository,
                             MedicalVisitRepository medicalVisitRepository,
                             DoctorMapper doctorMapper) {
        this.doctorRepository = doctorRepository;
        this.medicalVisitRepository = medicalVisitRepository;
        this.doctorMapper = doctorMapper;
    }

    @Override
    public List<DoctorOutDto> getAllDoctors() {
        return doctorMapper.toDtoList(doctorRepository.findAll());
    }

    @Override
    public Doctor getDoctorById(long id) throws EntityNotFoundException {
        return doctorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Doctor"));
    }

    @Override
    public Doctor saveDoctor(Doctor doctor) {
        ValidationHelper.validateDoctorSpecialties(doctor);
        ValidationHelper.checkUsernameUniqueness(doctorRepository.findByUsername(doctor.getUsername()));
        ValidationHelper.validateUsernameLength(doctor.getUsername());
        ValidationHelper.validateNameLength(doctor.getName());
        ValidationHelper.validatePassword(doctor.getPassword());
        try {
            return doctorRepository.save(doctor);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void deleteDoctor(long id) {
        doctorRepository.deleteById(id);
    }

    @Override
    public void updateDoctor(long id, Doctor doctor) throws EntityNotFoundException {
        Doctor existingDoctor = doctorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Doctor"));
        ValidationHelper.validateNameLength(doctor.getName());
        ValidationHelper.validatePassword(doctor.getPassword());
        ValidationHelper.validateDoctorSpecialties(doctor);
        existingDoctor.setSpecialties(doctor.getSpecialties());
        ValidationHelper.validateUsernameChange(existingDoctor.getUsername(), doctor.getUsername(),
                doctorRepository.findByUsername(doctor.getUsername()));
        existingDoctor.setName(doctor.getName());
        existingDoctor.setUsername(doctor.getUsername());
        existingDoctor.setPassword(doctor.getPassword());
        existingDoctor.setIsGp(doctor.getIsGp());
        doctorRepository.save(existingDoctor);
    }

    @Override
    public List<DoctorOutDto> getAllWithSpeciality(String specialty) {
        Specialty specialty1 = new Specialty();
        specialty1.setName(specialty);

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
}
