package org.example.medicalrecordproject.services.contracts;

import org.example.medicalrecordproject.dtos.in.creation.SpecialtyCreationDto;
import org.example.medicalrecordproject.dtos.out.response.SpecialtyResponseDto;
import org.example.medicalrecordproject.exceptions.EntityNotFoundException;
import org.example.medicalrecordproject.models.Specialty;

import java.util.List;

public interface SpecialtyService {
    List<SpecialtyResponseDto> getAllSpecialties();

    Specialty getSpecialtyById(long id) throws EntityNotFoundException;

    SpecialtyResponseDto getSpecialtyByIdResponse(long id) throws EntityNotFoundException;

    Specialty saveSpecialty(Specialty specialty);

    SpecialtyResponseDto createSpecialty(SpecialtyCreationDto dto);

    void deleteSpecialty(long id);

    void updateSpecialty(long id, Specialty specialty) throws EntityNotFoundException;

    Specialty getSpecialtyByName(String name) throws EntityNotFoundException;
}
