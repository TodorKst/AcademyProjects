package org.example.medicalrecordproject.services.contracts;

import org.example.medicalrecordproject.dtos.in.DoctorRegisterDto;
import org.example.medicalrecordproject.dtos.out.DoctorOutDto;
import org.example.medicalrecordproject.dtos.out.DoctorRegisteredDto;
import org.example.medicalrecordproject.dtos.out.DoctorStatOutDto;
import org.example.medicalrecordproject.exceptions.EntityNotFoundException;
import org.example.medicalrecordproject.models.users.Doctor;
import org.example.medicalrecordproject.repositories.DoctorRepository;

import java.sql.Timestamp;
import java.util.List;

public interface DoctorService {
    List<DoctorOutDto> getAllDoctors();

    Doctor getDoctorById(long id) throws EntityNotFoundException;

    DoctorRegisteredDto createDoctor(DoctorRegisterDto dto, Timestamp createdAt);

    Doctor saveDoctor(Doctor doctor);

    void deleteDoctor(long id);

    void updateDoctor(long id, Doctor doctor) throws EntityNotFoundException;

    List<DoctorOutDto> getAllWithSpeciality(String specialty);

    public List<DoctorOutDto> getAllGps();

    List<DoctorStatOutDto> countVisitsPerDoctor();

}
