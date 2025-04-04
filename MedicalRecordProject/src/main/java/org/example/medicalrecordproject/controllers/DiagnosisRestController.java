package org.example.medicalrecordproject.controllers;

import org.example.medicalrecordproject.dtos.in.creation.DiagnosisCreationDto;
import org.example.medicalrecordproject.dtos.out.DiagnosisStatOutDto;
import org.example.medicalrecordproject.dtos.out.response.DiagnosisResponseDto;
import org.example.medicalrecordproject.models.Diagnosis;
import org.example.medicalrecordproject.services.contracts.DiagnosisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
        return diagnosisService.getDiagnosisByIdResponse(id);
    }

    @PostMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public DiagnosisResponseDto createDiagnosis(@RequestBody DiagnosisCreationDto dto) {
        return diagnosisService.createDiagnosis(dto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteDiagnosis(@PathVariable long id) {
        diagnosisService.deleteDiagnosis(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void updateDiagnosis(@PathVariable long id, @RequestBody Diagnosis diagnosis) {
        diagnosisService.updateDiagnosis(id, diagnosis);
    }

    @GetMapping("/stats")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public List<DiagnosisStatOutDto> getDiagnosisStats() {
        return diagnosisService.getMostCommonDiagnoses();
    }
}
