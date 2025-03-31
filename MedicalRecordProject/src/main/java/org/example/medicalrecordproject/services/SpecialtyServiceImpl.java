package org.example.medicalrecordproject.services;

import org.example.medicalrecordproject.exceptions.EntityNotFoundException;
import org.example.medicalrecordproject.helpers.ValidationHelper;
import org.example.medicalrecordproject.models.Specialty;
import org.example.medicalrecordproject.repositories.SpecialtyRepository;
import org.example.medicalrecordproject.services.contracts.SpecialtyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpecialtyServiceImpl implements SpecialtyService {
    private final SpecialtyRepository specialtyRepository;

    @Autowired
    public SpecialtyServiceImpl(SpecialtyRepository specialtyRepository) {
        this.specialtyRepository = specialtyRepository;
    }

    @Override
    public List<Specialty> getAllSpecialties() {
        return specialtyRepository.findAll();
    }

    @Override
    public Specialty getSpecialtyById(long id) throws EntityNotFoundException {
        return specialtyRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Specialty"));
    }

    @Override
    public Specialty saveSpecialty(Specialty specialty) {
        ValidationHelper.validateNameLength(specialty.getName());
        return specialtyRepository.save(specialty);
    }

    @Override
    public void deleteSpecialty(long id) {
        specialtyRepository.deleteById(id);
    }

    @Override
    public void updateSpecialty(long id, Specialty specialty) throws EntityNotFoundException {
        Specialty existingSpecialty = specialtyRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(specialty.getName()));
        ValidationHelper.validateNameLength(specialty.getName());
        if (existingSpecialty != null) {
            existingSpecialty.setName(specialty.getName());
            specialtyRepository.save(existingSpecialty);
        }
    }

    @Override
    public Specialty getSpecialtyByName(String name) throws EntityNotFoundException {
        return specialtyRepository.findByName(name)
                .orElseThrow(() -> new EntityNotFoundException("Specialty"));
    }
}
