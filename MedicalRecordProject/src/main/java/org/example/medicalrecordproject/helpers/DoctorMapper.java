package org.example.medicalrecordproject.helpers;

import org.example.medicalrecordproject.dtos.out.DoctorOutDto;
import org.example.medicalrecordproject.models.users.Doctor;
import org.example.medicalrecordproject.models.Specialty;

import java.util.List;
import java.util.stream.Collectors;

public class DoctorMapper {

    public static DoctorOutDto toDto(Doctor doctor) {
        List<String> specialties = doctor.getSpecialties()
                .stream()
                .map(Specialty::getName)
                .collect(Collectors.toList());

        int patientCount = doctor.getPatients() != null ? doctor.getPatients().size() : 0;

        return new DoctorOutDto(
                doctor.getId(),
                doctor.getName(),
                doctor.getIsGp(),
                specialties,
                patientCount
        );
    }

    public static List<DoctorOutDto> toDtoList(List<Doctor> doctors) {
        return doctors.stream()
                .map(DoctorMapper::toDto)
                .collect(Collectors.toList());
    }
}
