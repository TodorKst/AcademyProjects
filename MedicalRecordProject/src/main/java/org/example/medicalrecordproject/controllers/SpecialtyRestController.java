package org.example.medicalrecordproject.controllers;

import org.example.medicalrecordproject.dtos.in.creation.SpecialtyCreationDto;
import org.example.medicalrecordproject.dtos.out.response.SpecialtyResponseDto;
import org.example.medicalrecordproject.models.Specialty;
import org.example.medicalrecordproject.services.contracts.SpecialtyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
    public List<SpecialtyResponseDto> getAllSpecialties() {
        return specialtyService.getAllSpecialties();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('DOCTOR', 'ADMIN')")
    public SpecialtyResponseDto getSpecialtyById(@PathVariable long id) {
        return specialtyService.getSpecialtyByIdResponse(id);
    }

    @PostMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public SpecialtyResponseDto createSpeciality(@RequestBody SpecialtyCreationDto dto) {
        return specialtyService.createSpecialty(dto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteSpecialty(@PathVariable long id) {
        specialtyService.deleteSpecialty(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void updateSpecialty(@PathVariable long id, @RequestBody Specialty specialty) {
        specialtyService.updateSpecialty(id, specialty);
    }

}
