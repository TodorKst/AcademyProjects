package org.example.medicalrecordproject.services;

import org.example.medicalrecordproject.dtos.in.creation.SpecialtyCreationDto;
import org.example.medicalrecordproject.dtos.out.creationresponse.SpecialtyResponseDto;
import org.example.medicalrecordproject.exceptions.EntityNotFoundException;
import org.example.medicalrecordproject.helpers.ValidationHelper;
import org.example.medicalrecordproject.helpers.mappers.EntityMapper;
import org.example.medicalrecordproject.models.Specialty;
import org.example.medicalrecordproject.repositories.SpecialtyRepository;
import org.example.medicalrecordproject.services.contracts.SpecialtyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpecialtyServiceImpl implements SpecialtyService {
    private final SpecialtyRepository specialtyRepository;
    private final ValidationHelper validationHelper;
    private final EntityMapper entityMapper;

    @Autowired
    public SpecialtyServiceImpl(SpecialtyRepository specialtyRepository,
                                ValidationHelper validationHelper, EntityMapper entityMapper) {
        this.specialtyRepository = specialtyRepository;
        this.validationHelper = validationHelper;
        this.entityMapper = entityMapper;
    }

    @Override
    public List<SpecialtyResponseDto> getAllSpecialties() {
        return entityMapper.toSpecialtyDtoList(specialtyRepository.findAll());
    }

    @Override
    public SpecialtyResponseDto getSpecialtyById(long id) throws EntityNotFoundException {
        return entityMapper.toSpecialtyDto(specialtyRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Specialty")));
    }

    @Override
    public Specialty saveSpecialty(Specialty specialty) {
        validationHelper.validateNameLength(specialty.getName());

        return specialtyRepository.save(specialty);
    }

    @Override
    public SpecialtyResponseDto createSpecialty(SpecialtyCreationDto dto) {
        Specialty specialty = entityMapper.toSpecialty(dto);
        specialtyRepository.save(specialty);

        return entityMapper.toSpecialtyDto(specialty);
    }

    @Override
    public void deleteSpecialty(long id) {
        specialtyRepository.deleteById(id);
    }

    @Override
    public void updateSpecialty(long id, Specialty specialty) throws EntityNotFoundException {
        Specialty existingSpecialty = specialtyRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(specialty.getName()));

        validationHelper.validateNameLength(specialty.getName());

        if (existingSpecialty != null) {
            existingSpecialty.setName(specialty.getName());
            specialtyRepository.save(existingSpecialty);
        }
    }

    @Override
    public SpecialtyResponseDto getSpecialtyByName(String name) throws EntityNotFoundException {
        return entityMapper.toSpecialtyDto(specialtyRepository.findByName(name)
                .orElseThrow(() -> new EntityNotFoundException("Specialty")));
    }
}
