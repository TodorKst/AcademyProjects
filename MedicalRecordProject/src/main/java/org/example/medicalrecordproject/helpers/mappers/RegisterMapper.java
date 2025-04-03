package org.example.medicalrecordproject.helpers.mappers;

import org.example.medicalrecordproject.dtos.in.creation.AdminCreationDto;
import org.example.medicalrecordproject.dtos.in.creation.PatientCreationDto;
import org.example.medicalrecordproject.dtos.out.response.AdminResponseDto;
import org.example.medicalrecordproject.dtos.out.response.DoctorResponseDto;
import org.example.medicalrecordproject.dtos.out.response.PatientResponseDto;
import org.example.medicalrecordproject.models.users.Admin;
import org.example.medicalrecordproject.models.users.Doctor;
import org.example.medicalrecordproject.models.users.Patient;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

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

    public Doctor toDoctor(org.example.medicalrecordproject.dtos.in.creation.DoctorCreationDto dto) {
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


    public AdminResponseDto toAdminDto(Admin admin) {
        return admin == null ? null : modelMapper.map(admin, AdminResponseDto.class);
    }

    public DoctorResponseDto toDoctorDto(Doctor doctor) {
        return doctor == null ? null : modelMapper.map(doctor, DoctorResponseDto.class);
    }

    public PatientResponseDto toPatientDto(Patient patient) {
        return patient == null ? null : modelMapper.map(patient, PatientResponseDto.class);
    }

    public List<PatientResponseDto> toPatientDtoList(List<Patient> patients) {
        return patients == null ? null : patients.stream()
                .map(this::toPatientDto)
                .toList();
    }
}
