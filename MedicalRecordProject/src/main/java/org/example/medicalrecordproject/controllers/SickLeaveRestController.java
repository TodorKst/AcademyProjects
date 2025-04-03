package org.example.medicalrecordproject.controllers;

import org.example.medicalrecordproject.dtos.in.creation.SickLeaveCreationDto;
import org.example.medicalrecordproject.dtos.out.DoctorStatOutDto;
import org.example.medicalrecordproject.dtos.out.MonthAndCountOutDto;
import org.example.medicalrecordproject.dtos.out.response.SickLeaveResponseDto;
import org.example.medicalrecordproject.exceptions.EntityNotFoundException;
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
    public List<SickLeaveResponseDto> getAllSickLeaves() {
        return sickLeaveService.getAllSickLeaves();
    }

    @GetMapping("/{id}")
    public SickLeaveResponseDto getSickLeaveById(@PathVariable long id) {
        try {
            return sickLeaveService.getSickLeaveByIdResponse(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PostMapping()
    @PreAuthorize("hasRole('DOCTOR') and @authHelper.isOwnerOfVisit(#sickLeave.medicalVisitId, authentication.name)")
    public SickLeaveResponseDto createSickLeave(@RequestBody SickLeaveCreationDto sickLeave) {
        return sickLeaveService.createSickLeave(sickLeave);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('DOCTOR') and @authHelper.isPatientOwnerOfSickLeave(#id, authentication.name)")
    public void updateSickLeave(@PathVariable long id, @RequestBody SickLeaveCreationDto dto) {
        try {
            sickLeaveService.updateSickLeave(id, dto);
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
