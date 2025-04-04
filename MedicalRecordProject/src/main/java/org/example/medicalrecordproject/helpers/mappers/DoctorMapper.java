package org.example.medicalrecordproject.helpers.mappers;

import org.example.medicalrecordproject.dtos.out.DoctorOutDto;
import org.example.medicalrecordproject.models.Specialty;
import org.example.medicalrecordproject.models.users.Doctor;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class DoctorMapper {

    private final ModelMapper modelMapper;

    public DoctorMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;

        // Converter for specialties -> List<String>
        Converter<Set<Specialty>, List<String>> specialtiesConverter = ctx -> {
            Set<Specialty> specialties = ctx.getSource();
            return specialties == null ? List.of() :
                    specialties.stream()
                            .map(Specialty::getName)
                            .collect(Collectors.toList());
        };

        // Configure the type map with custom specialty mapping and post converter for patient count
        this.modelMapper.typeMap(Doctor.class, DoctorOutDto.class)
                .addMappings(mapper -> mapper.using(specialtiesConverter)
                        .map(Doctor::getSpecialties, DoctorOutDto::setSpecialties))
                .setPostConverter(ctx -> {
                    Doctor source = ctx.getSource();
                    DoctorOutDto dest = ctx.getDestination();

                    try {
                        dest.setPatientCount(source.getPatients() != null ? source.getPatients().size() : 0);
                    } catch (Exception e) {
                        dest.setPatientCount(0); // Fallback in case of LazyInitializationException
                    }

                    return dest;
                });
    }

    public DoctorOutDto toDto(Doctor doctor) {
        return modelMapper.map(doctor, DoctorOutDto.class);
    }

    public List<DoctorOutDto> toDtoList(List<Doctor> doctors) {
        return doctors.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}
