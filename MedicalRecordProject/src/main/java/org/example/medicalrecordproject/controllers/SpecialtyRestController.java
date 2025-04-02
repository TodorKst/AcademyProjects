package org.example.medicalrecordproject.controllers;

import org.example.medicalrecordproject.exceptions.EntityNotFoundException;
import org.example.medicalrecordproject.models.Specialty;
import org.example.medicalrecordproject.services.contracts.SpecialtyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/specialties")
public class SpecialtyRestController {

    private final SpecialtyService specialtyService;

    @Autowired
    public SpecialtyRestController(SpecialtyService specialtyService) {
        this.specialtyService = specialtyService;
    }

    @GetMapping()
    @PreAuthorize("hasAnyRole('DOCTOR', 'ADMIN')")
    public List<Specialty> getAllSpecialties() {
        return specialtyService.getAllSpecialties();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('DOCTOR', 'ADMIN')")
    public Specialty getSpecialtyById(@PathVariable long id) {
        try {
            return specialtyService.getSpecialtyById(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PostMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public Specialty createSpeciality(@RequestBody Specialty specialty) {
        return specialtyService.saveSpecialty(specialty);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteSpecialty(@PathVariable long id) {
        try {
            specialtyService.deleteSpecialty(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void updateSpecialty(@PathVariable long id, @RequestBody Specialty specialty) {
        try {
            specialtyService.updateSpecialty(id, specialty);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

}
