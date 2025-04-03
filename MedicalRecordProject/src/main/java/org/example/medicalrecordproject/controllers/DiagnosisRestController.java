package org.example.medicalrecordproject.controllers;

import org.example.medicalrecordproject.dtos.in.creation.DiagnosisCreationDto;
import org.example.medicalrecordproject.dtos.out.DiagnosisStatOutDto;
import org.example.medicalrecordproject.dtos.out.response.DiagnosisResponseDto;
import org.example.medicalrecordproject.exceptions.EntityNotFoundException;
import org.example.medicalrecordproject.helpers.mappers.EntityMapper;
import org.example.medicalrecordproject.models.Diagnosis;
import org.example.medicalrecordproject.services.contracts.DiagnosisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/diagnoses")
public class DiagnosisRestController {

    private final DiagnosisService diagnosisService;

    @Autowired
    public DiagnosisRestController(DiagnosisService diagnosisService) {
        this.diagnosisService = diagnosisService;
    }

    @GetMapping()
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public List<DiagnosisResponseDto> getAllDiagnoses() {
        return diagnosisService.getAllDiagnoses();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public DiagnosisResponseDto getDiagnosisById(@PathVariable long id) {
        try {
            return diagnosisService.getDiagnosisByIdResponse(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PostMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public DiagnosisResponseDto createDiagnosis(@RequestBody DiagnosisCreationDto dto) {
        return diagnosisService.createDiagnosis(dto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteDiagnosis(@PathVariable long id) {
        try {
            diagnosisService.deleteDiagnosis(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void updateDiagnosis(@PathVariable long id, @RequestBody Diagnosis diagnosis) {
        try {
            diagnosisService.updateDiagnosis(id, diagnosis);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/stats")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public List<DiagnosisStatOutDto> getDiagnosisStats() {
        return diagnosisService.getMostCommonDiagnoses();
    }
}
