package org.example.medicalrecordproject.controllers;

import org.example.medicalrecordproject.dtos.out.DoctorStatOutDto;
import org.example.medicalrecordproject.dtos.out.MonthAndCountOutDto;
import org.example.medicalrecordproject.exceptions.EntityNotFoundException;
import org.example.medicalrecordproject.models.SickLeave;
import org.example.medicalrecordproject.services.contracts.SickLeaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    public SickLeave saveSickLeave(@RequestBody SickLeave sickLeave) {
        return sickLeaveService.saveSickLeave(sickLeave);
    }

    @DeleteMapping("/{id}")
    public void deleteSickLeave(@PathVariable long id) {
        try {
            sickLeaveService.deleteSickLeave(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/top-month")
    public MonthAndCountOutDto getTopMonth() {
        return sickLeaveService.getMonthWithMostSickLeaves();
    }
    
    @GetMapping("/top-doctors")
    public List<DoctorStatOutDto> getTopDoctorsBySickLeaves() {
        return sickLeaveService.getDoctorsWithMostSickLeaves();
    }
}
