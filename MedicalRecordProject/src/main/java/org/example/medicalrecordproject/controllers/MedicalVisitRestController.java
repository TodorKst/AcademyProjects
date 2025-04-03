package org.example.medicalrecordproject.controllers;

import org.example.medicalrecordproject.dtos.in.creation.MedicalVisitCreationDto;
import org.example.medicalrecordproject.dtos.out.response.MedicalVisitResponseDto;
import org.example.medicalrecordproject.exceptions.EntityNotFoundException;
import org.example.medicalrecordproject.models.MedicalVisit;
import org.example.medicalrecordproject.services.contracts.MedicalVisitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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

    //    this may be too much logic for a controller
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
        try {
            return medicalVisitService.getMedicalVisitByIdResponse(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PostMapping()
    @PreAuthorize("hasRole('DOCTOR') and @authHelper.isOwnerOfVisit(#id, authentication.name) or hasRole('ADMIN')")
    public MedicalVisitResponseDto createMedicalVisit(MedicalVisitCreationDto dto) {
        return medicalVisitService.createMedicalVisit(dto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('DOCTOR') and @authHelper.isOwnerOfVisit(#id, authentication.name) or hasRole('ADMIN')")
    public void deleteMedicalVisit(@PathVariable long id) {
        try {
            medicalVisitService.deleteMedicalVisit(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('DOCTOR') and @authHelper.isOwnerOfVisit(#id, authentication.name) or hasRole('ADMIN')")
    public void updateMedicalVisit(@PathVariable long id, @RequestBody MedicalVisitCreationDto medicalVisit) {
        try {
            medicalVisitService.updateMedicalVisit(id, medicalVisit);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/filter")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public List<MedicalVisitResponseDto> getVisitsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end,
            @RequestParam(required = false) Long doctorId
    ) {
        return medicalVisitService.getByDateRangeAndDoctor(start, end, doctorId);
    }

}
