package org.example.medicalrecordproject.services.contracts;

import org.example.medicalrecordproject.dtos.out.DoctorOutDto;
import org.example.medicalrecordproject.dtos.out.DoctorStatOutDto;
import org.example.medicalrecordproject.exceptions.EntityNotFoundException;
import org.example.medicalrecordproject.models.users.Doctor;

import java.util.List;

public interface DoctorService {
    List<DoctorOutDto> getAllDoctors();

    Doctor getDoctorById(long id) throws EntityNotFoundException;

    Doctor saveDoctor(Doctor doctor);

    void deleteDoctor(long id);

    void updateDoctor(long id, Doctor doctor) throws EntityNotFoundException;

    List<DoctorOutDto> getAllWithSpeciality(String specialty);

    public List<DoctorOutDto> getAllGps();

    List<DoctorStatOutDto> countVisitsPerDoctor();

}
