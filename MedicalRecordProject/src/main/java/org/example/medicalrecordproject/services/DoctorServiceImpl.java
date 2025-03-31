package org.example.medicalrecordproject.services;

import org.example.medicalrecordproject.dtos.out.DoctorStatOutDto;
import org.example.medicalrecordproject.exceptions.EntityNotFoundException;
import org.example.medicalrecordproject.models.Specialty;
import org.example.medicalrecordproject.models.users.Doctor;
import org.example.medicalrecordproject.repositories.DoctorRepository;
import org.example.medicalrecordproject.repositories.MedicalVisitRepository;
import org.example.medicalrecordproject.services.contracts.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepository;
    private final MedicalVisitRepository medicalVisitRepository;

    @Autowired
    public DoctorServiceImpl(DoctorRepository doctorRepository,
                             MedicalVisitRepository medicalVisitRepository) {
        this.doctorRepository = doctorRepository;
        this.medicalVisitRepository = medicalVisitRepository;

    }

    @Override
    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }

    @Override
    public Doctor getDoctorById(long id) throws EntityNotFoundException {
        return doctorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Doctor"));
    }

    @Override
    public Doctor saveDoctor(Doctor doctor) {
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
                .orElseThrow(() -> new EntityNotFoundException(doctor.getName()));
            existingDoctor.setName(doctor.getName());
            existingDoctor.setSpecialties(doctor.getSpecialties());
            doctor.setName(doctor.getName());
            doctor.setUsername(doctor.getUsername());
            doctor.setPassword(doctor.getPassword());
            doctor.setIsGp(doctor.getIsGp());
            doctorRepository.save(existingDoctor);
    }

    @Override
    public Doctor getAllWithSpeciality(String specialty) {
        Specialty specialty1 = new Specialty();
        specialty1.setName(specialty);
        return doctorRepository.findBySpecialtiesContains(specialty1);
    }

    @Override
    public List<Doctor> getAllGps() {
        return doctorRepository.findAllByIsGp(true);
    }

    @Override
    public List<DoctorStatOutDto> countVisitsPerDoctor() {
        return medicalVisitRepository.countVisitsPerDoctor()
                .stream()
                .map(row -> {
                    Long doctorId = (Long) row[0];
                    Long count = (Long) row[1];
                    String name = doctorRepository.findById(doctorId)
                            .orElseThrow(() -> new EntityNotFoundException("Doctor")).getName();
                    return new DoctorStatOutDto(doctorId, name, count);
                })
                .collect(Collectors.toList());
    }

}
