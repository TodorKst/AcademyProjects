package org.example.medicalrecordproject.services.contracts;

import org.example.medicalrecordproject.exceptions.EntityNotFoundException;
import org.example.medicalrecordproject.models.Specialty;

import java.util.List;

public interface SpecialtyService {
    List<Specialty> getAllSpecialties();

    Specialty getSpecialtyById(long id) throws EntityNotFoundException;

    Specialty saveSpecialty(Specialty specialty);

    void deleteSpecialty(long id);

    void updateSpecialty(long id, Specialty specialty) throws EntityNotFoundException;
}
