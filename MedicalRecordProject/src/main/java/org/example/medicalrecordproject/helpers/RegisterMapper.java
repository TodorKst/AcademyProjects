package org.example.medicalrecordproject.helpers;

import org.example.medicalrecordproject.dtos.in.AdminRegisterDto;
import org.example.medicalrecordproject.dtos.in.DoctorRegisterDto;
import org.example.medicalrecordproject.dtos.in.PatientRegisterDto;
import org.example.medicalrecordproject.dtos.out.AdminRegisteredDto;
import org.example.medicalrecordproject.dtos.out.DoctorRegisteredDto;
import org.example.medicalrecordproject.dtos.out.PatientRegisteredDto;
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

    public Patient toPatient(PatientRegisterDto dto) {
        if (dto == null) return null;
        Patient patient = modelMapper.map(dto, Patient.class);
        patient.setId(null); // ✅ Force insert
        return patient;
    }

    public Doctor toDoctor(DoctorRegisterDto dto) {
        if (dto == null) return null;
        Doctor doctor = modelMapper.map(dto, Doctor.class);
        doctor.setId(null); // ✅ Force insert
        return doctor;
    }

    public Admin toAdmin(AdminRegisterDto dto) {
        if (dto == null) return null;
        Admin admin = modelMapper.map(dto, Admin.class);
        admin.setId(null); // ✅ Force insert
        return admin;
    }


    public AdminRegisteredDto toAdminDto(Admin admin) {
        return admin == null ? null : modelMapper.map(admin, AdminRegisteredDto.class);
    }

    public DoctorRegisteredDto toDoctorDto(Doctor doctor) {
        return doctor == null ? null : modelMapper.map(doctor, DoctorRegisteredDto.class);
    }

    public PatientRegisteredDto toPatientDto(Patient patient) {
        return patient == null ? null : modelMapper.map(patient, PatientRegisteredDto.class);
    }
}
