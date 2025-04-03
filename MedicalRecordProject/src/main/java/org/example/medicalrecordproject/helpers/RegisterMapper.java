package org.example.medicalrecordproject.helpers;

import org.example.medicalrecordproject.dtos.in.AdminCreationDto;
import org.example.medicalrecordproject.dtos.in.DoctorCreationDto;
import org.example.medicalrecordproject.dtos.in.PatientCreationDto;
import org.example.medicalrecordproject.dtos.out.AdminCreationResponseDto;
import org.example.medicalrecordproject.dtos.out.DoctorCreationResponseDto;
import org.example.medicalrecordproject.dtos.out.PatientCreationResponseDto;
import org.example.medicalrecordproject.models.users.Admin;
import org.example.medicalrecordproject.models.users.Doctor;
import org.example.medicalrecordproject.models.users.Patient;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class RegisterMapper {

    private final ModelMapper modelMapper;

    public RegisterMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public Patient toPatient(PatientCreationDto dto) {
        if (dto == null) return null;
        Patient patient = modelMapper.map(dto, Patient.class);
        patient.setId(null); // ✅ Force insert
        return patient;
    }

    public Doctor toDoctor(DoctorCreationDto dto) {
        if (dto == null) return null;
        Doctor doctor = modelMapper.map(dto, Doctor.class);
        doctor.setId(null); // ✅ Force insert
        return doctor;
    }

    public Admin toAdmin(AdminCreationDto dto) {
        if (dto == null) return null;
        Admin admin = modelMapper.map(dto, Admin.class);
        admin.setId(null); // ✅ Force insert
        return admin;
    }


    public AdminCreationResponseDto toAdminDto(Admin admin) {
        return admin == null ? null : modelMapper.map(admin, AdminCreationResponseDto.class);
    }

    public DoctorCreationResponseDto toDoctorDto(Doctor doctor) {
        return doctor == null ? null : modelMapper.map(doctor, DoctorCreationResponseDto.class);
    }

    public PatientCreationResponseDto toPatientDto(Patient patient) {
        return patient == null ? null : modelMapper.map(patient, PatientCreationResponseDto.class);
    }
}
