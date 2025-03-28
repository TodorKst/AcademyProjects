package org.example.medicalrecordproject.services;

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
    public Specialty getSpecialtyById(long id) {
        return specialtyRepository.findById(id).orElse(null);
    }

    @Override
    public Specialty saveSpecialty(Specialty specialty) {
        return specialtyRepository.save(specialty);
    }

    @Override
    public void deleteSpecialty(long id) {
        specialtyRepository.deleteById(id);
    }

    @Override
    public void updateSpecialty(long id, Specialty specialty) {
        Specialty existingSpecialty = specialtyRepository.findById(id).orElse(null);
        if (existingSpecialty != null) {
            existingSpecialty.setName(specialty.getName());
            specialtyRepository.save(existingSpecialty);
        }
    }
}
