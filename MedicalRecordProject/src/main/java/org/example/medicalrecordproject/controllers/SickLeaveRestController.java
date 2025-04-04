package org.example.medicalrecordproject.controllers;

import org.example.medicalrecordproject.dtos.in.creation.SickLeaveCreationDto;
import org.example.medicalrecordproject.dtos.out.DoctorStatOutDto;
import org.example.medicalrecordproject.dtos.out.MonthAndCountOutDto;
import org.example.medicalrecordproject.dtos.out.response.SickLeaveResponseDto;
import org.example.medicalrecordproject.services.contracts.SickLeaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.Instant;
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
    @PreAuthorize("hasAnyRole('DOCTOR', 'ADMIN')")
    public SickLeaveResponseDto getSickLeaveById(@PathVariable long id) {
        return sickLeaveService.getSickLeaveByIdResponse(id);
    }

    @PostMapping()
    @PreAuthorize("hasRole('DOCTOR') and @authHelper.isOwnerOfVisit(#sickLeave.medicalVisitId, authentication.name) or hasRole('ADMIN')")
    public SickLeaveResponseDto createSickLeave(@RequestBody SickLeaveCreationDto sickLeave) {
        return sickLeaveService.createSickLeave(sickLeave, Timestamp.from(Instant.now()));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('DOCTOR') and @authHelper.isPatientOwnerOfSickLeave(#id, authentication.name) or hasRole('ADMIN')")
    public void updateSickLeave(@PathVariable long id, @RequestBody SickLeaveCreationDto dto) {
        sickLeaveService.updateSickLeave(id, dto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('DOCTOR') and @authHelper.isPatientOwnerOfSickLeave(#id, authentication.name) or hasRole('ADMIN')")
    public void deleteSickLeave(@PathVariable long id) {
        sickLeaveService.deleteSickLeave(id);
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
