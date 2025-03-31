package org.example.medicalrecordproject.helpers;

import org.example.medicalrecordproject.dtos.in.AdminRegisterDto;
import org.example.medicalrecordproject.dtos.in.DoctorRegisterDto;
import org.example.medicalrecordproject.dtos.in.PatientRegisterDto;
import org.example.medicalrecordproject.models.users.Admin;
import org.example.medicalrecordproject.models.users.Doctor;
import org.example.medicalrecordproject.models.users.Patient;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, builder = @Builder(disableBuilder = true))
public interface RegisterMapper {

    // Map fields from AdminRegisterDto to Admin (inherited from User)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "role", ignore = true)
    Admin toAdmin(AdminRegisterDto dto);

    // Map fields from DoctorRegisterDto to Doctor
    @Mapping(target = "specialties", ignore = true)
    @Mapping(target = "patients", ignore = true)
    @Mapping(target = "medicalVisits", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "role", ignore = true)
    Doctor toDoctor(DoctorRegisterDto dto);

    // Map fields from PatientRegisterDto to Patient
    @Mapping(target = "gp", ignore = true)
    @Mapping(target = "lastInsurancePayment", ignore = true)
    @Mapping(target = "medicalVisits", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "role", ignore = true)
    Patient toPatient(PatientRegisterDto dto);
}
