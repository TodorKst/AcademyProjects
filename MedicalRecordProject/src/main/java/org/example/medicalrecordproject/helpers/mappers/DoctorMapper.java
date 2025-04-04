package org.example.medicalrecordproject.helpers.mappers;

import org.example.medicalrecordproject.dtos.out.DoctorOutDto;
import org.example.medicalrecordproject.models.Specialty;
import org.example.medicalrecordproject.models.users.Doctor;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class DoctorMapper {

    private final ModelMapper modelMapper;

    public DoctorMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;

        this.modelMapper.addMappings(new PropertyMap<Doctor, DoctorOutDto>() {
            @Override
            protected void configure() {
                map().setPatientCount(source.getPatients() != null ? source.getPatients().size() : 0);
            }
        });

        Converter<Set<Specialty>, List<String>> specialtiesConverter = ctx -> {
            Set<Specialty> specialties = ctx.getSource();
            return specialties == null ? List.of() :
                    specialties.stream()
                            .map(Specialty::getName)
                            .collect(Collectors.toList());
        };

        this.modelMapper.typeMap(Doctor.class, DoctorOutDto.class)
                .addMappings(mapper -> mapper.using(specialtiesConverter).map(Doctor::getSpecialties, DoctorOutDto::setSpecialties));
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
