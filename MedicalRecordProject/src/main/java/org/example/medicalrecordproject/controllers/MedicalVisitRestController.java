package org.example.medicalrecordproject.controllers;

import org.example.medicalrecordproject.exceptions.EntityNotFoundException;
import org.example.medicalrecordproject.models.MedicalVisit;
import org.example.medicalrecordproject.services.contracts.MedicalVisitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
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
    public List<MedicalVisit> getMedicalVisits(
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
    public MedicalVisit getMedicalVisitById(@PathVariable long id) {
        try {
            return medicalVisitService.getMedicalVisitById(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PostMapping()
    public MedicalVisit saveMedicalVisit(MedicalVisit medicalVisit) {
        return medicalVisitService.saveMedicalVisit(medicalVisit);
    }

    @DeleteMapping("/{id}")
    public void deleteMedicalVisit(@PathVariable long id) {
        try {
            medicalVisitService.deleteMedicalVisit(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public void updateMedicalVisit(@PathVariable long id, @RequestBody MedicalVisit medicalVisit) {
        try {
            medicalVisitService.updateMedicalVisit(id, medicalVisit);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

 }
