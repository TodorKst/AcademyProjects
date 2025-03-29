package org.example.medicalrecordproject.services;

import org.example.medicalrecordproject.exceptions.EntityNotFoundException;
import org.example.medicalrecordproject.models.Specialty;
import org.example.medicalrecordproject.models.users.Doctor;
import org.example.medicalrecordproject.repositories.DoctorRepository;
import org.example.medicalrecordproject.services.contracts.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepository;

    @Autowired
    public DoctorServiceImpl(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
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
}
