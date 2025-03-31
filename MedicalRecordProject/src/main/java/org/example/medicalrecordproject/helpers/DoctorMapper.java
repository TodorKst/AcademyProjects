package org.example.medicalrecordproject.helpers;

import org.example.medicalrecordproject.dtos.out.DoctorOutDto;
import org.example.medicalrecordproject.models.users.Doctor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface DoctorMapper {

    @Mapping(target = "specialties", source = "specialties", qualifiedByName = "mapSpecialties")
    @Mapping(target = "patientCount", source = "patients", qualifiedByName = "countPatients")
    DoctorOutDto toDto(Doctor doctor);

    List<DoctorOutDto> toDtoList(List<Doctor> doctors);

    @Named("mapSpecialties")
    static List<String> mapSpecialties(Set<?> specialties) {
        return specialties.stream()
                .map(s -> s.toString())
                .collect(Collectors.toList());
    }

    @Named("countPatients")
    static int countPatients(List<?> patients) {
        return patients == null ? 0 : patients.size();
    }
}
