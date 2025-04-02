package org.example.medicalrecordproject.controllers;

import org.example.medicalrecordproject.dtos.out.DoctorStatOutDto;
import org.example.medicalrecordproject.dtos.out.MonthAndCountOutDto;
import org.example.medicalrecordproject.exceptions.EntityNotFoundException;
import org.example.medicalrecordproject.models.SickLeave;
import org.example.medicalrecordproject.services.contracts.SickLeaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/sick-leaves")
public class SickLeaveRestController {

    private final SickLeaveService sickLeaveService;

    @Autowired
    public SickLeaveRestController(SickLeaveService sickLeaveService) {
        this.sickLeaveService = sickLeaveService;
    }

    @GetMapping()
    @PreAuthorize("hasAnyRole('DOCTOR', 'ADMIN')")
    public List<SickLeave> getAllSickLeaves() {
        return sickLeaveService.getAllSickLeaves();
    }

    @GetMapping("/{id}")
    public SickLeave getSickLeaveById(@PathVariable long id) {
        try {
            return sickLeaveService.getSickLeaveById(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PostMapping()
    @PreAuthorize("hasRole('DOCTOR') and @authHelper.isOwnerOfVisit(#sickLeave.medicalVisit.id, authentication.name)")
    public SickLeave createSickLeave(@RequestBody SickLeave sickLeave) {
        return sickLeaveService.saveSickLeave(sickLeave);
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('DOCTOR') and @authHelper.isPatientOwnerOfSickLeave(#id, authentication.name)")
    public SickLeave updateSickLeave(@PathVariable long id, @RequestBody SickLeave sickLeave) {
        try {
            return sickLeaveService.updateSickLeave(id, sickLeave);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('DOCTOR') and @authHelper.isPatientOwnerOfSickLeave(#id, authentication.name)")
    public void deleteSickLeave(@PathVariable long id) {
        try {
            sickLeaveService.deleteSickLeave(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/top-month")
    @PreAuthorize("hasRole('ADMIN')")
    public MonthAndCountOutDto getTopMonth() {
        return sickLeaveService.getMonthWithMostSickLeaves();
    }

    @GetMapping("/top-doctors")
    @PreAuthorize("hasRole('ADMIN')")
    public List<DoctorStatOutDto> getTopDoctorsBySickLeaves() {
        return sickLeaveService.getDoctorsWithMostSickLeaves();
    }
}
