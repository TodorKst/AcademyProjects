package org.example.medicalrecordproject.controllers;

import org.example.medicalrecordproject.dtos.in.creation.DiagnosisCreationDto;
import org.example.medicalrecordproject.dtos.in.creation.MedicalVisitCreationDto;
import org.example.medicalrecordproject.dtos.out.response.MedicalVisitResponseDto;
import org.example.medicalrecordproject.services.contracts.MedicalVisitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/medical-visits")
public class MedicalVisitRestController {

    private final MedicalVisitService medicalVisitService;

    @Autowired
    public MedicalVisitRestController(MedicalVisitService medicalVisitService) {
        this.medicalVisitService = medicalVisitService;
    }


    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public List<MedicalVisitResponseDto> getMedicalVisits(
            @RequestParam(required = false) Long patientId,
            @RequestParam(required = false) Long doctorId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {

        if (patientId != null) {
            return medicalVisitService.getByPatientId(patientId);
        }
        if (doctorId != null) {
            return medicalVisitService.getByDoctorId(doctorId);
        }
        if (startDate != null && endDate != null) {
            return medicalVisitService.getByVisitDateBetween(startDate, endDate);
        }

        return medicalVisitService.getAllMedicalVisits();
    }


    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR') or @authHelper.isPatientOwner(#id, authentication.name)")
    public MedicalVisitResponseDto getMedicalVisitById(@PathVariable long id) {
        return medicalVisitService.getMedicalVisitByIdResponse(id);
    }

    @PostMapping()
    @PreAuthorize("hasRole('DOCTOR') and @authHelper.isOwnerOfVisit(#id, authentication.name) or hasRole('ADMIN')")
    public MedicalVisitResponseDto createMedicalVisit(MedicalVisitCreationDto dto) {
        return medicalVisitService.createMedicalVisit(dto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('DOCTOR') and @authHelper.isOwnerOfVisit(#id, authentication.name) or hasRole('ADMIN')")
    public void deleteMedicalVisit(@PathVariable long id) {
        medicalVisitService.deleteMedicalVisit(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('DOCTOR') and @authHelper.isOwnerOfVisit(#id, authentication.name) or hasRole('ADMIN')")
    public void updateMedicalVisit(@PathVariable long id, @RequestBody MedicalVisitCreationDto medicalVisit) {
        medicalVisitService.updateMedicalVisit(id, medicalVisit);
    }

    @PostMapping("/{id}/diagnoses")
    @PreAuthorize("hasRole('DOCTOR') and @authHelper.isOwnerOfVisit(#id, authentication.name) or hasRole('ADMIN')")
    public MedicalVisitResponseDto addDiagnosis(@PathVariable long id, @RequestBody DiagnosisCreationDto dto) {
        return medicalVisitService.addDiagnosis(id, dto);
    }

}
