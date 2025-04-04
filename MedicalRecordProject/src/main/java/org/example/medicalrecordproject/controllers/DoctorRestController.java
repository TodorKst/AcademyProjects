package org.example.medicalrecordproject.controllers;

import org.example.medicalrecordproject.dtos.in.creation.DoctorCreationDto;
import org.example.medicalrecordproject.dtos.out.DoctorOutDto;
import org.example.medicalrecordproject.dtos.out.DoctorStatOutDto;
import org.example.medicalrecordproject.dtos.out.response.DoctorResponseDto;
import org.example.medicalrecordproject.dtos.out.response.MedicalVisitResponseDto;
import org.example.medicalrecordproject.models.users.Doctor;
import org.example.medicalrecordproject.services.contracts.DoctorService;
import org.example.medicalrecordproject.services.contracts.MedicalVisitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.Instant;
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
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public List<DoctorResponseDto> getAllDoctors() {
        return doctorService.getAllDoctors();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public DoctorResponseDto getDoctorById(@PathVariable long id) {

        return doctorService.getDoctorByIdResponse(id);

    }

    @PostMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public DoctorResponseDto createDoctor(@RequestBody DoctorCreationDto dto) {
        return doctorService.createDoctor(dto, Timestamp.from(Instant.now()));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteDoctor(@PathVariable long id) {
        doctorService.deleteDoctor(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public DoctorResponseDto updateDoctor(@PathVariable long id, @RequestBody Doctor doctor) {
        return doctorService.updateDoctor(id, doctor);
    }

    @GetMapping("/{id}/medical-visits")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public List<MedicalVisitResponseDto> getMedicalVisitsByDoctorId(@PathVariable long id) {
        return medicalVisitService.getByDoctorId(id);
    }

    @GetMapping("/by-specialty")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public List<DoctorOutDto> getAllWithSpeciality(@RequestParam String specialty) {
        return doctorService.getAllWithSpeciality(specialty);
    }

    @GetMapping("/gps")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public List<DoctorOutDto> getAllGps() {
        return doctorService.getAllGps();
    }

    @GetMapping("/visit-stats")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public List<DoctorStatOutDto> getVisitCountPerDoctor() {
        return doctorService.countVisitsPerDoctor();
    }

    @GetMapping("/most-sickleaves")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public List<DoctorOutDto> getDoctorsWithMostSickLeaves() {
        return doctorService.getDoctorsWithMostSickLeaves();
    }
}
