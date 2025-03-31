package org.example.medicalrecordproject.controllers;

import org.example.medicalrecordproject.dtos.out.DoctorStatOutDto;
import org.example.medicalrecordproject.exceptions.EntityNotFoundException;
import org.example.medicalrecordproject.models.MedicalVisit;
import org.example.medicalrecordproject.models.users.Doctor;
import org.example.medicalrecordproject.services.contracts.DoctorService;
import org.example.medicalrecordproject.services.contracts.MedicalVisitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/doctors")
public class DoctorRestController {

    private final DoctorService doctorService;
    private final MedicalVisitService medicalVisitService;

    @Autowired
    public DoctorRestController(DoctorService doctorService,
                                MedicalVisitService medicalVisitService) {
        this.doctorService = doctorService;
        this.medicalVisitService = medicalVisitService;
    }

    @GetMapping()
    public List<Doctor> getAllDoctors() {
        return doctorService.getAllDoctors();
    }

    @GetMapping("/{id}")
    public Doctor getDoctorById(@PathVariable long id) {
        try {
            return doctorService.getDoctorById(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PostMapping()
    public Doctor saveDoctor(@RequestBody Doctor doctor) {
        return doctorService.saveDoctor(doctor);
    }

    @DeleteMapping("/{id}")
    public void deleteDoctor(@PathVariable long id) {
        try {
            doctorService.deleteDoctor(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public void updateDoctor(@PathVariable long id, @RequestBody Doctor doctor) {
        try {
            doctorService.updateDoctor(id, doctor);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/{id}/medical-visits")
    public List<MedicalVisit> getMedicalVisitsByDoctorId(@PathVariable long id) {
        return medicalVisitService.getByDoctorId(id);
    }

    @GetMapping("/by-specialty")
    public Doctor getAllWithSpeciality(@RequestParam String specialty) {
        return doctorService.getAllWithSpeciality(specialty);
    }

    @GetMapping("/gps")
    public List<Doctor> getAllGps() {
        return doctorService.getAllGps();
    }

    @GetMapping("/visit-stats")
    public List<DoctorStatOutDto> getVisitCountPerDoctor() {
        return doctorService.countVisitsPerDoctor();
    }
}
